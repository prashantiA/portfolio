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
  const response = await fetch('/display-comments');
  const content = await response.json();
  const elem = document.getElementById('comments-content');
  elem.innerHTML = '';
  let index = 0;
  while (index < content.length) {
    elem.appendChild(formatComment(content[index]));
    index++;
  }
}

function formatComment(comment) {
  var text = document.createTextNode(comment.content);
  const commentElem = document.createElement('p');
  commentElem.id = comment.id;
  commentElem.appendChild(text);
  return commentElem;
}

function start() {
  typeWriter();
  loadComments();
}
