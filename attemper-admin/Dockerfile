FROM nginx

COPY dist /usr/share/app-admin/src/dist/
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

EXPOSE 5200

CMD ["nginx","-g","daemon off;"]
