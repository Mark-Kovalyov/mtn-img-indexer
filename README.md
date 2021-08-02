# XATTR/Attr

```bash
$ attr -l git.c
Attribute "xattr-foo" has a 11 byte value for git.c

$ attr -s key01 -V value01 git.c
Attribute "key01" set to a 7 byte value for git.c:
value01

$ attr -l git.c
Attribute "key01" has a 7 byte value for git.c
Attribute "xattr-foo" has a 11 byte value for git.c
```
