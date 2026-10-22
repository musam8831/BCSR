#!/usr/bin/env python3
"""Sign and verify catalogue bundles with Ed25519 (T-M4-006; ADR-0005 §4, §5, §8).

  keygen --key-id ID --private PATH --public PATH   new key pair; PATH for the private key must be OUTSIDE the repo
  sign   --bundle FILE --private PATH                writes FILE with .json replaced by .sig (base64 of the 64-byte signature)
  verify --bundle FILE --public PATH                 checks the signature over the exact file bytes, then the manifest

The private key never enters the repository or CI (ADR-0005 §4): keygen and sign refuse a private-key path inside the repo.
Exit 0 = ok, 1 = verification or input failure.
"""
import argparse
import base64
import json
import pathlib
import sys

from cryptography.exceptions import InvalidSignature
from cryptography.hazmat.primitives import serialization
from cryptography.hazmat.primitives.asymmetric.ed25519 import Ed25519PrivateKey, Ed25519PublicKey

ROOT = pathlib.Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "scripts"))
from build_catalogue_bundle import items_digest  # noqa: E402


def _inside_repo(path):
    try:
        pathlib.Path(path).resolve().relative_to(ROOT)
        return True
    except ValueError:
        return False


def keygen(key_id, private_path, public_path):
    if _inside_repo(private_path):
        raise SystemExit("Refusing: the private key must be stored outside the repository (ADR-0005 §4).")
    key = Ed25519PrivateKey.generate()
    pathlib.Path(private_path).write_bytes(key.private_bytes(
        serialization.Encoding.PEM, serialization.PrivateFormat.PKCS8, serialization.NoEncryption()))
    pub = key.public_key().public_bytes(serialization.Encoding.Raw, serialization.PublicFormat.Raw)
    pathlib.Path(public_path).write_text(f"{key_id} {base64.b64encode(pub).decode()}\n", encoding="utf-8", newline="\n")


def load_public(public_path):
    key_id, b64 = pathlib.Path(public_path).read_text(encoding="utf-8").split()
    return key_id, Ed25519PublicKey.from_public_bytes(base64.b64decode(b64))


def sig_path(bundle):
    return pathlib.Path(str(bundle)[:-len(".json")] + ".sig") if str(bundle).endswith(".json") else pathlib.Path(str(bundle) + ".sig")


def sign(bundle, private_path):
    if _inside_repo(private_path):
        raise SystemExit("Refusing: the private key must be stored outside the repository (ADR-0005 §4).")
    key = serialization.load_pem_private_key(pathlib.Path(private_path).read_bytes(), password=None)
    data = pathlib.Path(bundle).read_bytes()
    out = sig_path(bundle)
    out.write_text(base64.b64encode(key.sign(data)).decode() + "\n", encoding="ascii", newline="\n")
    return out


def verify(bundle, public_path):
    """Returns a list of errors; empty means the bundle may be loaded (mirrors the app's load rules, ADR-0005 §5)."""
    key_id, pub = load_public(public_path)
    data = pathlib.Path(bundle).read_bytes()
    try:
        sig = base64.b64decode(sig_path(bundle).read_text(encoding="ascii").strip())
        pub.verify(sig, data)
    except FileNotFoundError:
        return ["signature file missing"]
    except (InvalidSignature, ValueError):
        return ["signature does not match the bundle bytes"]
    errors = []
    b = json.loads(data.decode("utf-8"))
    m, items = b.get("manifest", {}), b.get("items", [])
    if m.get("key_id") != key_id:
        errors.append(f"manifest key_id {m.get('key_id')} does not match public key {key_id}")
    if m.get("item_count") != len(items):
        errors.append("manifest item_count does not match the items")
    if m.get("items_sha256") != items_digest(items):
        errors.append("manifest items_sha256 does not match the items")
    if any((i.get("verification") or {}).get("status") != "approved" for i in items):
        errors.append("bundle contains a non-approved item")
    return errors


def main(argv=None):
    ap = argparse.ArgumentParser()
    sub = ap.add_subparsers(dest="cmd", required=True)
    k = sub.add_parser("keygen"); k.add_argument("--key-id", required=True); k.add_argument("--private", required=True); k.add_argument("--public", required=True)
    s = sub.add_parser("sign"); s.add_argument("--bundle", required=True); s.add_argument("--private", required=True)
    v = sub.add_parser("verify"); v.add_argument("--bundle", required=True); v.add_argument("--public", required=True)
    a = ap.parse_args(argv)
    if a.cmd == "keygen":
        keygen(a.key_id, a.private, a.public)
        print(f"Key pair written. Keep {a.private} offline with a backup (docs/09-release-operations/catalogue-signing.md).")
        return 0
    if a.cmd == "sign":
        print(f"Wrote {sign(a.bundle, a.private)}")
        return 0
    errors = verify(a.bundle, a.public)
    print("OK: signature and manifest valid" if not errors else "INVALID:\n" + "\n".join(errors))
    return 1 if errors else 0


if __name__ == "__main__":
    sys.exit(main())
