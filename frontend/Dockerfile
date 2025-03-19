FROM node:latest AS build

WORKDIR /app

COPY file-sharing/ /app/

RUN npm install

RUN npm run build

FROM nginx:latest

COPY --from=build /app/dist/file-sharing/browser /usr/share/nginx/html

COPY nginx.conf /etc/nginx/conf.d/default.conf