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

prev = null;

function addFunFact() {
  const facts =
      ['I have a younger sister named Aruna!', 'My favorite ice cream flavor is mint chocolate chip!', 
	      'I\'ve been to four continents!', 'My favorite series is Harry Potter!', 'I\'m from the Bay Area!',
              'I can speak French!', 'I\'m part of CMU\'s KPOP dance club!'];

  // Pick a random greeting.
  index = null;
  if (prev != null) {
    index = Math.floor(Math.random() * (facts.length - 1));
    if (index >= prev) index += 1;
  }
  else {
    index = Math.floor(Math.random() * (facts.length));
  }
  const fact = facts[index];
  console.log('this is running');
  // Add it to the page.
  const factContainer = document.getElementById('fact');
  factContainer.innerText = fact;
  prev = index;  
}
