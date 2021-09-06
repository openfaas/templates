'use strict'

module.exports = function main(data) {
  return {
    root: {
      type: "container",
      children: [{
        type: "text",
        value: `hello ${data.value}`
      }]
    }
  }
}