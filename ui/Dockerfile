## build stage
FROM node:20 AS build
WORKDIR /app

# install dependencies
COPY package*.json ./
RUN npm ci

# copy sources and build
COPY . .
RUN npm run build

## runtime stage
FROM nginx:1.27-alpine

# copy conf and build
COPY nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80