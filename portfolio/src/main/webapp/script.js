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

let prev = null;

function addFunFact() {
  const facts =
      [`I have an amazing younger sister named Aruna!`, `My favorite ice cream flavor is mint chocolate chip!`, 
	      `I've been to four continents!`, `My favorite series is Harry Potter!`, `I'm from the Bay Area!`,
              `I can speak French!`, `I'm part of CMU's KPOP dance club!`];

  // Pick a random greeting.
  let index = null;
  let seed = Math.random();
  if (prev != null) {
    index = Math.floor(seed * (facts.length - 1));
    if (index >= prev) index += 1;
  }
  else {
    index = Math.floor(seed * (facts.length));
  }
  let fact = facts[index];
  // Add it to the page.
  let elem = document.getElementById('fact');
  $(elem).animate({'opacity': 0}, 1000, function () {$(elem).text(fact);}).animate({'opacity': 1}, 1000);
  prev = index;  
}

let typewriterIndex = 0;
let typewriterElem = null;
const TYPEWRITER_TEXT = 'Welcome to my personal portfolio!';
const TYPEWRITER_SPEED = 75;

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

  const response = await fetch('/display-comments?num=' + numToFetch);
  const content = await response.json();

  const elem = document.getElementById('comments-content');
  elem.innerHTML = '';

  for (index = 0; index < content.length; index++) {
    elem.appendChild(formatComment(content[index]));
  }

  document.getElementById('quantity').value = content.length;
}

function formatComment(comment) {
  let text = document.createElement('p');
  text.textContent = comment.content;
  const commentElem = document.createElement('div');
  commentElem.classList.add('comment');
  commentElem.id = comment.id;
  commentElem.appendChild(text);

  let button = document.createElement('button');
  button.textContent = "Delete Comment";
  button.classList.add('comment-button');
  button.onclick = function() {deleteOneComment(comment.id)};
  commentElem.appendChild(button);
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

function start() {
  typeWriter();

  /* Short timeout to address latency issues after adding comments
   * Prevents most issues with comments not appearing after they were added
   * No noticeable lag
   */
  setTimeout(loadComments, 100);
}
