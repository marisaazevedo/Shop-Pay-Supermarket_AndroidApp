const usersDB = require('../database/dbops');
const KoaRouter = require('koa-router');

const router = new KoaRouter();

router
  .get('/', rootHello)
  .get('/users', allUsers)
  .get('/users/:id', oneUser)
  .post('/users', addUser)
  .delete('/users/:id', delUser)
  .put('/users/:id', changeUser)

async function rootHello(ctx) {
  console.log('in routed rootHello()');
  const res = await usersDB.dbTest(2);
  ctx.body = 'Welcome to this server root API (test 2nd entry: ' + res + ')';
}

async function allUsers(ctx) {
  console.log('in allUsers()');
  var result = await usersDB.getAll();
  if ('errno' in result) {
    result = [];
    ctx.status = 404;            // means not found
  }
  console.log(result);
  ctx.body = result;
}

async function oneUser(ctx) {
  console.log('in oneUser()');
  var result = await usersDB.getUserById(ctx.params.id);
  if (Object.keys(result).length === 0 || 'errno' in result) {
    result = {};
    ctx.status = 404;            // means not found
  }
  ctx.body = result;
}

async function addUser(ctx) {
  console.log('in addUser()');
  const name = ctx.request.body;
  console.log(name);

  var result = await usersDB.addNewUser(name);
  if ('errno' in result || result.lastID === 0) {
    ctx.status = 400;            // means simply bad request
    ctx.body = {};
  }
  else
    ctx.body = { Id: result.lastID, Name: name };
}

async function delUser(ctx) {
  console.log('in delUser()');
  var result = await usersDB.deleteUserById(ctx.params.id);
  if ('errno' in result || result.changes === 0) {
    ctx.status = 404;            // means not found
  }
  ctx.body = {};
}

async function changeUser(ctx) {
  console.log('in changeUser()');
  const name = ctx.request.body;
  console.log('New Name: ' + name);

  var result = await usersDB.updateUserById(ctx.params.id, name)
  if ('errno' in result || result.changes === 0) {
    ctx.status = 404;            // means not found
    ctx.body = {};
  }
  else
    ctx.body = {Id: Number(ctx.params.id), Name: name};
}

module.exports = router;