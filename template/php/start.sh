#!/bin/bash

# Set custom webroot
if [ ! -z "$WEBROOT" ]; then
 sed -i "s#root /home/app;#root ${WEBROOT};#g" /etc/nginx/sites-available/default.conf
fi

# Display PHP error's or not
if [[ "$ERRORS" != "1" ]] ; then
 echo php_flag[display_errors] = off >> /etc/php7/php-fpm.conf
else
 echo php_flag[display_errors] = on >> /etc/php7/php-fpm.conf
fi

# Very dirty hack to replace variables in code with ENVIRONMENT values
if [[ "$TEMPLATE_NGINX_HTML" == "1" ]] ; then
  for i in $(env)
  do
    variable=$(echo "$i" | cut -d'=' -f1)
    value=$(echo "$i" | cut -d'=' -f2)
    if [[ "$variable" != '%s' ]] ; then
      replace='\$\$_'${variable}'_\$\$'
      find /var/www/html -type f -exec sed -i -e 's/'${replace}'/'${value}'/g' {} \;
    fi
  done
fi

# Start supervisord and services
/usr/bin/supervisord -n -c /etc/supervisord.conf