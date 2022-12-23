'use strict';

require('dotenv').config();
const express = require('express');
const cors = require('cors');
const userRoute = require('./routes/userRoute');
const postRoute = require('./routes/postRoute');
const foodFactRoute = require('./routes/foodFactRoute');
const authorizationRoute = require('./routes/authorizationRoute');
const messageRoute = require('./routes/messageRoute');
const passport = require('./utilities/pass');
const app = express();
const port = process.env.PORT || 3000;

//Port number decision
process.env.NODE_ENV = process.env.NODE_ENV || 'development';
if (process.env.NODE_ENV === 'production') {
  require('./utilities/production')(app, process.env.PORT || 3000);
} else {
  require('./utilities/localhost')(app, port);
}

app.use(cors());
app.use(express.json());
app.use(express.urlencoded({extended:true}));
app.use(passport.initialize());
app.use('/thumbnails', express.static('thumbnails'));
app.use(express.static('uploads'));

// Non-authenticated routes
app.use('/auth', authorizationRoute);
app.get('/', (req, res) => {
  if (req.secure) {
    res.send('Hello Secure World!');
  } else {
    res.send('not secured?');
  }
});

// Partially authenticated routes
app.use('/post', postRoute);
app.use('/food', foodFactRoute);

// Strictly authenticated routes
app.use('/user', passport.authenticate('jwt', {session: false}), userRoute);
app.use('/message', passport.authenticate('jwt', {session: false}), messageRoute);