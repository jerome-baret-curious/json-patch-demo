# Handling PATCH requests

> [!WARNING]
> Best practices are not followed!

Both [JSON Patch](https://datatracker.ietf.org/doc/html/rfc6902) and [JSON Merge Patch](https://datatracker.ietf.org/doc/html/rfc7396) are tested here.

Create a tester, e.g.
```json
{
  "name": "NOME",
  "work": {
    "name": "BestWork"
  }
}
```

Use JSON Patch:
```json
[
  {
  "op": "replace",
  "path": "/name",
  "value": "myname"
  }
]
```

Use JSON Merge Patch:
```json
{
  "work": null
}
```