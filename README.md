### Nametag Mobile Challenge

Build a mobile app for DuckIt (described below). We have deployed the backend at https://nametag-duckit-2.uc.r.appspot.com/

Please write what you consider to be production quality code, whatever that means to you. You can build an iOS, Android, or React Native app. Your solution should run in the Android emulator / iOS simulator for reasonably modern phones. Don't worry about getting an app into TestFlight or sending us an APK, we'll build and run it ourselves. We'll swap in our own provisioning profiles and developer accounts, if needed.

As in real life, answers to clarifying questions will be elusive. If you have a question, write it down, and guess what you think my answer would be.

## **Problem Statement**

**Duckit**: Reddit, but for ducks!

As we all know, the Internet exists exclusively for photos of cats. But what about pictures of ducks? For far too long, ducks have been left out. Our new startup will revolutionize the world of duck related photo sharing.

1. The main view is a long list of the pictures. Each picture consists of an image, a headline, the number of upvotes, and an upvote and downvote button.
2. You must be logged in to upvote and downvote, but you don’t need to be logged in to see duck photos.
3. The login screen is pretty much like every other one on the internet. Ask for their email address and password. If the account doesn’t exist, create it.
4. The signin and signup APIs give you back a token that you can use for other API requests. When the user returns to the site, we’d like them to stay logged in.
5. (Bonus) a page to create a new post. Ask for a photo and headline.

We hired the best designer and came up with the following wireframe to give you a sense of what we are looking for:

!https://s3-us-west-2.amazonaws.com/secure.notion-static.com/49d00cf4-24ed-441e-8629-8c4f44b299db/Untitled.png

Luckily, the backend API has already been built for you.

### **Sign in**

- Method: POST
- URI: `/signin`
- Request body: `{"email": "...", "password": "..."}`
- Response 200: `{"token": "....."}`
- Response 403: password incorrect
- Response 404: account not found

### **Sign up**

- Method: POST
- URI: `/signup`
- Request body: `{"email": "...", "password": "..."}`
- Response 200: `{"token": "....."}`
- Response 409: account already exists

### **Get posts**

- Method: GET
- URI: `/posts`
- Request header: `Authorization: Bearer <token>` // optional
- Response 200: `{“posts”: [{"id": <string>, "headline": "...", "image": "https://...."}]}`

### **Upvote**

- Method: POST
- URI: `/posts/:id/upvote`
- Request header: `Authorization: Bearer <token>`
- Response 200: `{“upvotes”: 11}`

### **Downvote**

- Method: POST
- URI: `/posts/:id/downvote`
- Request header: `Authorization: Bearer <token>`
- Response 200: `{“upvotes”: 9}`

### **New post**

- Method: POST
- URI: `/posts`
- Request header: `Authorization: Bearer <token>`
- Request body: `{“headline”: “...”, “image”: "<url>"}`
- Response 201: success

The backend is available at [https://nametag-duckit-2.uc.r.appspot.com](https://nametag-duckit-2.uc.r.appspot.com/)