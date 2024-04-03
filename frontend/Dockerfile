# Step 1: Installing dependencies and creating a production build
FROM node:21-alpine3.19 AS build

WORKDIR /user/src/app

# Copy package.json and package-lock.json (or yarn.lock) files
COPY package.json package-lock.json ./

# Install all dependencies, including 'devDependencies' required for building the project
RUN npm ci

# Copy the rest of your app's source code
COPY . .

# Build the app for production
RUN npm run build

# Step 3: Running the application
FROM node:21-alpine3.19 AS run

WORKDIR /user/src/app

# Copy the production build files from the build stage
COPY --from=build /user/src/app/dist ./dist

# Copy package.json and package-lock.json (or yarn.lock) files
COPY package.json package-lock.json ./

# Install only production dependencies
RUN npm ci --omit=dev

# Install 'serve' to serve the app on port 4173
RUN npm install -g serve

CMD ["serve", "-s", "dist", "-l", "4173"]
