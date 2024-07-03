# MacOS上无法启动测试，报错：Library not loaded: /usr/local/opt/openssl/lib/libssl.1.0.0.dylib
```bash
brew install rbenv/tap/openssl@1.0

ln -sfn /usr/local/Cellar/openssl@1.0/1.0.2u /usr/local/opt/openssl
```

