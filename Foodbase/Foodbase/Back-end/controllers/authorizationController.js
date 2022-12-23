'use strict';

// Authors Reima N. Vili M.

const jwt = require('jsonwebtoken');
const passport = require('passport');
const userModel = require('../models/userModel');
const foodFactModel = require('../models/foodFactModel');
const bcryptjs = require('bcryptjs');
const {validationResult} = require("express-validator");

// function to login
const login = (req, res, next) => {
  passport.authenticate('local', {session: false}, (error, user) => {
    //In case of errors or missing user
    if (error || !user) {
      return res.status(400).json({
        message: `Authentication failed due to bad credentials`
      });
    }
    //If no errors
    req.login(user, {session: false}, async (error) => {
      if (error)  {
        res.send(error);
      }
      const token = jwt.sign(user, process.env.JWT_SECRET);
      const preferences = await foodFactModel.getUserFoodFacts(user.ID);
      return res.json({user, token, preferences});
    });

  })(req, res, next);
};

// function to create a user
const userCreate_post = async (req, res) => {

  // Extract the validation errors from a request.
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    console.log('user create error', errors);
    res.send(errors.array());
  } else {
    // bcrypted password
    const cryptedPass = await bcryptjs.hash(req.body.password, 13);

    // something to hold the user base data and preferences separate
    const user = {};
    const prefIDS = [];
    user.username = req.body.username;
    user.email = req.body.email;
    user.password = cryptedPass;
    user.area = req.body.area;
    delete req.body.username;
    delete req.body.email;
    delete req.body.password;
    delete req.body.area;

    // after deleting other post info theres only preferences left in req.body
    for (const prefsKey in req.body) {
      prefIDS.push(parseInt(prefsKey));
    }

    // get existing emails to an array
    const existingUsers = await userModel.getAllUsers();
    const existingEmailsArr = [];
    for (const existingUsersKey in existingUsers) {
      existingEmailsArr.push(existingUsers[existingUsersKey].email);
    }
    let result = false;
    // test if the email is already in use
    if (!existingEmailsArr.some( ai => user.email.includes(ai))) {
      // create the main user data
      result = await userModel.createUser(user, res);
    }

    if (result.affectedRows === 1) {
      let prefsToInsert = [];
      console.log('result id', result.insertId)
      for (let i = 0; i < prefIDS.length; i++) {
        prefsToInsert.push([result.insertId, prefIDS[i]]);
      }
      let preferences = 0;
      // create preferences for the user
      if (prefsToInsert.length > 0) {
        preferences = await userModel.createUserPreferences(prefsToInsert, res);
      }
      console.log('preferences created for user:', preferences);
      res.json({createSuccessful: true});
    } else {
      res.json({error: 'Login error: Contact website administrators'});
    }
  }
};

module.exports = {
  login,
  userCreate_post,
};