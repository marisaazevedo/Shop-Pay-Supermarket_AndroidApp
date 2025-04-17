'use strict';
const path = require('path');

let db;

(async () => {
  const { Database } = await import('sqlite-async');
  db = await Database.open(path.resolve('database/users.db'));
  console.log('connected to users.db');
})();

class DBOps {
  async dbTest(id) {
    const result = await db.get("select Name from Users where Id=?", [id]);
    console.log(result);
    return result.Name;
  }

  async getAll() {
    var result;
    try {
      result = await db.all("select * from Users");  // array of objects with the table rows
    }
    catch (err) {
      result = err;
    }
    return result;
  }

  async getUserById(id) {
    var result;
    try {
      result = await db.get("select Name from Users where Id=?", [id]);  // one object with the selected fields
      if (result == null)
        result = {};
    }
    catch (err) {
      result = err;
    }
    return result;
  }

  async addNewUser(name) {
    var result;
    try {
      result = await db.run('insert into Users(Name) values(?)', [name]);
      if (result.changes === 0)
        result.lastID = 0;
    }
    catch (err) {
      result = err;
    }
    return result;
  }

  async deleteUserById(id) {
    var result;
    try {
      result = await db.run('delete from Users where Id=?', [id]);
    }
    catch (err) {
      result = err;
    }
    return result;
  }

  async updateUserById(id, name) {
    var result;
    try {
      result = await db.run('update Users set Name=? where Id=?', [name, id])
    }
    catch (err) {
      result = err;
    }
    return result;
  }
}

module.exports = new DBOps();