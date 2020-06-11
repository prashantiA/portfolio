// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

/**
 * Adds a random fun fact to the page.
 */

let prevFactIndex = null;
let commentPage = 0;
let pageCursors = [];
let prevNumToFetch = 10;
let imageUploadUrl = null;
let typewriterIndex = 0;
let typewriterElem = null;
const TYPEWRITER_TEXT = 'Welcome to my personal portfolio!';
const TYPEWRITER_SPEED = 75;

function addFunFact() {
  const facts =
      [`I have an amazing younger sister named Aruna!`, `My favorite ice cream flavor is mint chocolate chip!`, 
	      `I've been to four continents!`, `My favorite series is Harry Potter!`, `I'm from the Bay Area!`,
              `I can speak French!`, `I'm part of CMU's KPOP dance club!`];

  // Pick a random greeting.
  let index = null;
  let seed = Math.random();
  if (prevFactIndex != null) {
    index = Math.floor(seed * (facts.length - 1));
    if (index >= prevFactIndex) index += 1;
  }
  else {
    index = Math.floor(seed * (facts.length));
  }
  let fact = facts[index];
  // Add it to the page.
  let elem = document.getElementById('fact');
  $(elem).animate({'opacity': 0}, 1000, function () {$(elem).text(fact);}).animate({'opacity': 1}, 1000);
  prevFactIndex = index;  
}

function typeWriter() {
  if (typewriterIndex === 0) {
    typewriterElem = document.getElementById('welcome-header');
    typewriterElem.innerHTML = '';
  }
  if (typewriterIndex < TYPEWRITER_TEXT.length) {
    typewriterElem.innerHTML += TYPEWRITER_TEXT.charAt(typewriterIndex);
    typewriterIndex++;
    setTimeout(typeWriter, TYPEWRITER_SPEED);
  }
}

async function loadComments() {
  let numToFetch = document.getElementById('quantity').value;
  if (numToFetch !== prevNumToFetch) {
    commentPage = 0;
    prevNumToFetch = numToFetch;
  }
  await makeValidPage();

  const userResponse = await fetch('/user-info');
  const userRes = await userResponse.json();
  const isAdmin = userRes.isAdmin;

  if (userRes.isLoggedIn) {
    document.getElementById('nickname-form').style.display = 'block';
    res = await fetch('/nicknames');
    let nickname = await res.text();
    document.getElementById('nickname-input').value = nickname;
  } else {
    document.getElementById('nickname-form').style.display = 'none';
    document.getElementById('nickname-input').value = '';
  }


  queryString = '/display-comments?num=' + numToFetch;  
  if (commentPage !== 0) {
    queryString += '&page=' + pageCursors[commentPage];
  }
	
  const response = await fetch(queryString);
  const content = await response.json();

  const elem = document.getElementById('comments-content');
  elem.innerHTML = '';

  for (index = 0; index < (content.comments).length; index++) {
    elem.appendChild(formatComment((content.comments)[index], isAdmin));
  }

  if (isAdmin) {
    document.getElementById('comments-delete-all').innerHTML = '<button class="centered" onclick="deleteAllComments()">Delete All Comments</button>'
  }
  document.getElementById('quantity').value = numToFetch;

  pageCursors[commentPage+1] = content.cursor;
}

function formatComment(comment, isAdmin) {

  let nickname = document.createElement('p');
  let boldText = document.createElement('b');
  boldText.textContent = comment.author;
  nickname.appendChild(boldText);

  let text = document.createElement('p');
  text.textContent = comment.content;
  const commentElem = document.createElement('div');
  commentElem.classList.add('comment');
  commentElem.id = comment.id;
  commentElem.appendChild(nickname);
  commentElem.appendChild(text);

  if (comment.hasOwnProperty("image")) {
    let image = document.createElement('img');
    image.src = comment.image;
    image.classList.add('comment-image');
    commentElem.appendChild(image);
    commentElem.appendChild(document.createElement('br'));
  }

  if (isAdmin) {
    let button = document.createElement('button');
    button.textContent = "Delete Comment";
    button.classList.add('comment-button');
    button.onclick = function() {deleteOneComment(comment.id)};
    commentElem.appendChild(button);
  }
  return commentElem;
}
  

async function deleteAllComments() {
  const response = await fetch('/delete-comments?type=all', {method : 'post'});
  
  /* Timeout selected experimentally to prevent most issues with latency
   * There are still some latency issues but too much lag was introduced by making the timeout longer
   */
  setTimeout(loadComments, 1000);
}

async function deleteOneComment(id) {
  const response = await fetch('/delete-comments?type=byId&id='+id, {method : 'post'});

  setTimeout(loadComments, 100);
}
	
async function makeValidPage() {
  let response = await fetch('/comment-info');
  let numComments = await response.json();
  
  let pageSize = document.getElementById('quantity').value;
  let maxPage = Math.ceil(numComments[0]/pageSize) - 1;
  
  commentPage = Math.max(0, Math.min(maxPage, commentPage));
}

function nextPage() {
  commentPage++;
  loadComments();
}

function prevPage() {
  commentPage--;
  loadComments();
}

async function addComment() {
  let content = document.getElementById('comment-text').value;
  if (content.replace(/\s/g, '') === '' && document.getElementById('image').files.length === 0) {
    return;
  }
 
  let data = new FormData(document.getElementById('add-comment-form'));

  if (document.getElementById('nickname-input').value.replace(/\s/g, '') !== '') {
    await setNickname();
    data.append("author", document.getElementById('nickname-input').value);
  }
  
  let fetchUrl = '/add-comment';
  if (imageUploadUrl !== null) {
    fetchUrl = imageUploadUrl;
  }
  await fetch(fetchUrl, {method: 'post', body: data});
  
  document.getElementById('add-comment-form').reset();
  setTimeout(loadComments, 500);
  fetchBlobstoreUrlAndShowForm();
}

async function loadLogin() {
  let response = await fetch('/userapi');
  let res = await response.text();
  document.getElementById('login').innerHTML = res;
}

async function setNickname() {
  let nickname = document.getElementById('nickname-input').value;
  let resp = await fetch('/nicknames?nickname=' + nickname, {method: 'post'});
}


async function fetchBlobstoreUrlAndShowForm() {
  const resp = await fetch('/blobstore-upload-url');
  imageUploadUrl = await resp.text();
  document.getElementById('image').style.display = 'block';
}

function start() {
  typeWriter();
  loadComments();
  loadLogin();
  fetchBlobstoreUrlAndShowForm();
}
