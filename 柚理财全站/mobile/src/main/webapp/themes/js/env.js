var envurlinfo ={
    hosturl : "/",
    hostname : window.location.hostname,
    urltest : /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])$/
   }
if(envurlinfo.hostname === "localhost" || envurlinfo.urltest.test(envurlinfo.hostname) || envurlinfo.hostname === ""){
  envurlinfo.hosturl = "http://127.0.0.1:8989/";
}

export{envurlinfo}