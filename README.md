# JavaSocketAPI

## An api for java with the native Java socket technology and the [boonproject](https://github.com/boonproject/boon).

### Implementation

Download the latest jar file from the releases tab and implement it in your project. Make sure to also implement the jar file when building your project.

### Example



Let's start by creating two packets, one for requesting the current time and one for actually returning it. We do this by creating the classes and extending it from Packet. After we did this, we need to implement the methods and the constructor. Note that the default constructor has to be there because it is used for system recognition. You can implement as many custom constructors as you like, but inside them, you need to always set the connectionUUID to null in the super call. In the send method, you just write every variable you want to send to the writingByteBuffer and in the recieve method you initialize them again reading them from the readingByteBuffer. You must read and write the variables in the same order because otherwise, the system couldn't handle it correctly.
