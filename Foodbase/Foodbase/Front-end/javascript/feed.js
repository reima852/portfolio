'use strict';

// Authors Karoliina M. & Reima N.

// Select most important stuff
const allergensDiv = document.querySelector('#allergens');
const dietsDiv = document.querySelector('#diets');
const sessionPreferences = JSON.parse(sessionStorage.getItem('preferences'));
const sessionUser = JSON.parse(sessionStorage.getItem('user'));

//------Functionality for searchBar---------------------------------------------
// Mobile search form
const mobileSearchForm = document.querySelector('#mobile-search-form');

// Mobile search submit
mobileSearchForm.addEventListener('submit', evt =>  {
  evt.preventDefault();
  const mobileSearchFormData = new FormData(mobileSearchForm);
  // Start but don't await the asynchronous function to prevent UI sluggishness
  searchArticlesBySearchTerms(mobileSearchFormData);
})

// Desktop search form
const desktopSearchBar = document.querySelector('#search-text-bar-desktop');
const desktopSearchButton = document.querySelector('.nav-search-button');

// desktop search submit
desktopSearchButton.addEventListener('click', evt =>  {
  evt.preventDefault();
  // Get the preferences from the mobileSearchForm but replace the keywords
  const desktopSearchFormData = new FormData(mobileSearchForm);
  desktopSearchFormData.delete('keywords');
  desktopSearchFormData.append('keywords', desktopSearchBar.value);
  // Start but don't await the asynchronous function to prevent UI sluggishness
  searchArticlesBySearchTerms(desktopSearchFormData);
})
// Visual search slide-in
function showSearch() {
  mobileSearchForm.style.display = 'block';
}

//------Post generation function------------------------------------------------
const postFeed = document.querySelector('.post-feed');
const generatePosts = async (posts) => {
  //First clear the old posts
  postFeed.innerHTML = '';
  if (!(posts.length === 0)) {
    //Here we generate the posts
    await postGenerator(postFeed, posts, true, true, false, false);
  } else {
    // If no posts show up notify the user.
    const promptMessage = document.createElement('h6');
    promptMessage.className = 'post-title';
    promptMessage.textContent = 'Looks like not a single post showed up. Try changing your preferences!'
    postFeed.appendChild(promptMessage);
  }
}

//------Function for generating filtered posts----------------------------------

const getFilteredPosts = async () => {
  // Only send pref ID's
  const preferencesToBeSent = {};
  for (const pref in sessionPreferences) {
    const prefName = sessionPreferences[pref].ID;
    preferencesToBeSent[prefName] = true;
  }

  try {
    const fetchOptions = {
      method: 'POST',
      headers: {
        Authorization: 'Bearer ' + sessionStorage.getItem('token'),
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(preferencesToBeSent),
    };
    const response = await fetch(url + '/post/search', fetchOptions);
    const posts = await response.json();
    // Lazy generation
    generatePosts(posts);
  } catch (e) {
    console.log(e.message);
  }
}

//------Test if user is logged in and act accordingly---------------------------
if (!sessionUser) {
  // Generate checkbox lists without precheck
  generateCheckBoxList(allergensDiv,0);
  generateCheckBoxList(dietsDiv,1);
  // Hide the logout button
  const logoutLi = document.querySelector('.logout');
  logoutLi.style.display = 'none';
  //creating loginIcon instead of logout
  createLoginIcon(logoutLi);
  // Get and generate all posts to feed
  const getAllPosts = async () => {
    try {
      const fetchOptions = {
        headers: {
          Authorization: 'Bearer ' + sessionStorage.getItem('token'),
        },
      };
      const response = await fetch(url + '/post', fetchOptions);
      const posts = await response.json();
      //Here we generate the posts
      await postGenerator(postFeed, posts, true, true, false, false);
    } catch (e) {
      console.log(e.message);
    }
  };
  // Initially at page load if user not logged in
  getAllPosts();

} else {
  // Generate prechecked checkbox lists
  generateCheckBoxListWithPreCheck(allergensDiv, 0, sessionPreferences);
  generateCheckBoxListWithPreCheck(dietsDiv, 1, sessionPreferences);

  // Get and generate all user preference matching posts
  getFilteredPosts();
}

//------Function for searching posts--------------------------------------------
const searchArticlesBySearchTerms = async (searchForm) => {
  try {
    const fetchOptions = {
      method: 'POST',
      headers: {},
      body: searchForm,
    };
    const response = await fetch(url + '/post/search', fetchOptions);
    const posts = await response.json();
    // Lazy generation
    generatePosts(posts);
  } catch (e) {
    console.log(e.message);
  }
}