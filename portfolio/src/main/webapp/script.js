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

function getComments() {
  const val = document.getElementById("comment-number").value;
  fetch('/data?numOfComments='+val).then(response => response.json()).then((jsonData) => {
      const commentsListElement = document.getElementById('comment-list');
      commentsListElement.innerHTML = '';
      for(let i = 0; i < jsonData.length; i++) {
        commentsListElement.appendChild(createListElement(jsonData[i]));
      } 
  });
}

function createListElement(text) {
  const liElement = document.createElement('li');
  liElement.innerText = text;
  return liElement;
}
