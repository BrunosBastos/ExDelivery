FROM node:12.2.0-alpine

# set working directory
WORKDIR ./

# add `//node_modules/.bin` to $PATH
ENV PATH ./node_modules/.bin:$PATH

COPY package.json yarn.lock ./

#use the minified build file for production, not now - npm start is for development.
#COPY ./build/* ./public/

#install dependencies:

RUN npm install yarn

RUN yarn install

# Arguments
ARG REACT_APP_API_BASE_URL
ENV REACT_APP_API_BASE_URL=${REACT_APP_API_BASE_URL}
ENV REACT_APP_RUNNING_MODE="PRODUCTION"


#copy your project files: (also bad for development, use volume(https://docs.docker.com/storage/volumes/) instead)
COPY . .

# start

CMD ["yarn", "start"]
