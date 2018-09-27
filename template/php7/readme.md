# PHP7 Template

Templates are built using the latest minor version of the major release.

| Modules (by default) |
| ------------- |
| Core, date, libxml, openssl, pcre, sqlite3, zlib, ctype, curl, dom, fileinfo, filter, ftp, hash, iconv, json, mbstring, SPL, PDO, pdo_sqlite, session, posix, readline, Reflection, standard, SimpleXML, Phar, tokenizer, xml, xmlreader, xmlwriter, mysqlnd, sodium |

## Usage:

```shell
faas-cli new my-function-php7 --lang php7
```

You will find in the newly created directories `my-function-in-php5` and `my-function-in-php7`:
- `src/Handler.php` : entrypoint
- `src/php-extension.sh` : is for installing PHP extensions if needed
- `src/composer.json` : is for dependency management

## Extra Extensions

If you need to install Phalcon for example, check out the following gist - you can use
that file in your function into your `src/php-extension.sh` file;

https://gist.github.com/andrew-s/07e152b7f981e8453da8d2d991d9aab4

## Private Composer Auth

In some cases, you may need to use private composer repositories - using the faas-cli you can pass in
a build argument during build - something like;

```
faas-cli build -f ./functions.yml --build-arg COMPOSER_AUTH='{"bitbucket-oauth": {"bitbucket.org": {"consumer-key": "xxxxxxxx","consumer-secret": "xxxxxxx"}}}'
```
See more information at; https://getcomposer.org/doc/05-repositories.md#git-alternatives

That way you can pass in tokens for composer, if necessary, GitHub tokens to get around rate-limit issues.
