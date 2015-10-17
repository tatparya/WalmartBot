# WalmartBot
Walmart Challenge Android Bot

Challenge : 
\tGiven a product description / name, reply to the text with product price guess within 15% error to receive 1 point. 
Implementation :
\tImplemented Broadcast Receivers to listen for SMS notifications, parse messages and extract product description. Querying the Walmart API using the product description and parsing the JSON object received for product price.

Sending messages from an within an app :
`       
    SmsManager sm = SmsManager.getDefault();
    String number = "<Phone Number>";
    String msg = "<Message Text>";
    sm.sendTextMessage(number, null, msg, null, null);
`
