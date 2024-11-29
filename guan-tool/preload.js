var countVocabulary = () => {
  let msg='单词数量：';
  const fs = require('fs');
  const data = fs.readFileSync('/Volumes/HomeWork/transfer/EnglishStudy/vocabulary/myCOCA1.txt', 'utf-8');
  
      let count=0;
      const lines = data.split(/\r?\n/);
      lines.forEach((line) => {
        count++;
      });
      // console.log(count);
      msg += count;
      console.log(msg);
  return msg;
}
window.addEventListener('DOMContentLoaded', () => {

    const replaceText = (selector, text) => {
      const element = document.getElementById(selector)
      if (element) element.innerText = text
    }
    let msg=countVocabulary();
    replaceText(`msg`, msg)
   
  })
