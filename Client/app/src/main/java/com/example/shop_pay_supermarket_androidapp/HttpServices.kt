package com.example.shop_pay_supermarket_androidapp

import java.io.*
import java.net.HttpURLConnection
import java.net.URL

private fun readStream(input: InputStream): String {
  var reader: BufferedReader? = null
  var line: String?
  val response = StringBuilder()
  try {
    reader = BufferedReader(InputStreamReader(input))
    while (reader.readLine().also { line = it } != null)
      response.append(line)
  }
  catch (e: IOException) {
    response.clear()
    response.append("readStream: ${e.message}")
  }
  reader?.close()
  return response.toString()
}
/*
//**************************************************************************
// Function to call REST operation GetUsers
fun getUsers(act: MainActivity, baseAddress: String, port: Int) {
  val urlRoute = "/users"
  val url = URL("http://$baseAddress:$port$urlRoute")
  act.writeText("GET ${url.toExternalForm()}")

  var urlConnection: HttpURLConnection? = null
  try {
    urlConnection = (url.openConnection() as HttpURLConnection).apply {
      doInput = true
      setRequestProperty("Content-Type", "application/json")
      useCaches = false
      connectTimeout = 5000
      if (responseCode == 200)
        act.appendText(readStream(inputStream))
      else
        act.appendText("Code: $responseCode")
    }
  } catch (e: Exception) {
    act.appendText(e.toString())
  }
  urlConnection?.disconnect()
}

//**************************************************************************
// Function to call REST operation GetUser
fun getUser(act: MainActivity, baseAddress: String, port: Int, userId: String) {
  val urlRoute = "/users"
  val url = URL("http://$baseAddress:$port$urlRoute/$userId")
  act.writeText("GET ${url.toExternalForm()}")

  var urlConnection: HttpURLConnection? = null
  try {
    urlConnection = (url.openConnection() as HttpURLConnection).apply {
      doInput = true
      setRequestProperty("Content-Type", "application/json")
      useCaches = false
      connectTimeout = 5000
      if (responseCode == 200)
        act.appendText(readStream(inputStream))
      else
        act.appendText("Code: $responseCode")
    }
  }
  catch (e: Exception) {
    act.appendText(e.toString())
  }
  urlConnection?.disconnect()
}

//**************************************************************************
// Function to call REST operation AddUser
fun addUser(act: MainActivity, baseAddress: String, port: Int, userName: String) {
  val urlRoute = "/users"
  val url = URL("http://$baseAddress:$port$urlRoute")
  act.writeText("POST ${url.toExternalForm()}")
  val payload = "\"$userName\""
  act.appendText("payload: $payload")

  var urlConnection: HttpURLConnection? = null
  try {
    urlConnection = (url.openConnection() as HttpURLConnection).apply {
      doOutput = true
      doInput = true
      requestMethod = "POST"
      setRequestProperty("Content-Type", "application/json")
      useCaches = false
      connectTimeout = 5000
      with(outputStream) {
        write(payload.toByteArray())
        flush()
        close()
      }
      // get response
      if (responseCode == 200)
        act.appendText(readStream(inputStream))
      else
        act.appendText("Code: $responseCode")
    }
  }
  catch (e: Exception) {
    act.appendText(e.toString())
  }
  urlConnection?.disconnect()
}

//**************************************************************************
// Function to call REST operation DeleteUser
fun delUser(act: MainActivity, baseAddress: String, port: Int, userId: String) {
  val urlRoute = "/users"
  val url = URL("http://$baseAddress:$port$urlRoute/$userId")
  act.writeText("DELETE ${url.toExternalForm()}")

  var urlConnection: HttpURLConnection? = null
  try {
    urlConnection = (url.openConnection() as HttpURLConnection).apply {
      requestMethod = "DELETE"
      setRequestProperty("Content-Type", "application/json")
      useCaches = false
      connectTimeout = 5000

      // get response
      act.appendText("Code: $responseCode")
    }
  }
  catch (e: Exception) {
    act.appendText(e.toString())
  }
  urlConnection?.disconnect()
}

//**************************************************************************
// Function to call REST operation ChangeUser
fun chUser(act: MainActivity, baseAddress: String, port: Int, userId: String, userName: String) {
  val urlRoute = "/users"
  val url = URL("http://$baseAddress:$port$urlRoute/$userId")
  val payload = "\"" + userName + "\""
  act.writeText("PUT ${url.toExternalForm()}")
  act.appendText("Payload: $payload")

  var urlConnection: HttpURLConnection? = null
  try {
    urlConnection = (url.openConnection() as HttpURLConnection).apply {
      doOutput = true
      doInput = true
      requestMethod = "PUT"
      setRequestProperty("Content-Type", "application/json")
      useCaches = false
      connectTimeout = 5000

      with(outputStream) {
        write(payload.toByteArray())
        flush()
        close()
      }

      // get response
      if (responseCode == 200)
        act.appendText(readStream(inputStream))
      else
        act.appendText("Code: $responseCode")
    }
  } catch (e: Exception) {
    act.appendText(e.toString())
  }
  urlConnection?.disconnect()
}
*/