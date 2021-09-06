'use strict'

const initData = require('./InitData');

module.exports = async (action, data, props, event) => {
  switch (action) {
    case "InitData":
      return initData();
    default:
      return {};
  }
}
