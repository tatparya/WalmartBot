# WalmartBot
<h3>Walmart Challenge Android Bot<h3/>

<h3>Challenge : <h3/>

Given a product description / name, reply to the text with product price guess within 15% error to receive 1 point. 
<h3>Implementation : <h3/>

Implemented Broadcast Receivers to listen for SMS notifications, parse messages and extract product description. Querying the Walmart API using the product description and parsing the JSON object received for product price.

<h4>Sending messages from an within an app :<h4/>

```  
    SmsManager sm = SmsManager.getDefault();
    String number = "<Phone Number>";
    String msg = "<Message Text>";
    sm.sendTextMessage(number, null, msg, null, null);
```
