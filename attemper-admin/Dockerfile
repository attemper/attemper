FROM nginx

COPY dist /usr/share/app/src/dist/
RUN chmod -R 755 /usr/share/app/src/dist
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 80

CMD ["nginx","-g","daemon off;"]
