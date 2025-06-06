package Client.WhatsTheWord.client.admin;


import Client.WhatsTheWord.client.ClientCallback;
import Client.WhatsTheWord.client.ClientCallbackHelper;
import Client.WhatsTheWord.referenceClasses.Admin;
import Client.WhatsTheWord.referenceClasses.AdminHelper;

/**
* WhatsTheWord/client/admin/AdminServicePOA.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WhatsTheWord.idl
* Thursday, May 15, 2025 6:17:05 PM SGT
*/

public abstract class AdminServicePOA extends org.omg.PortableServer.Servant
 implements AdminServiceOperations, org.omg.CORBA.portable.InvokeHandler
{

  // Constructors

  private static java.util.Hashtable _methods = new java.util.Hashtable ();
  static
  {
    _methods.put ("request", new Integer (0));
  }

  public org.omg.CORBA.portable.OutputStream _invoke (String $method,
                                org.omg.CORBA.portable.InputStream in,
                                org.omg.CORBA.portable.ResponseHandler $rh)
  {
    org.omg.CORBA.portable.OutputStream out = null;
    Integer __method = (Integer)_methods.get ($method);
    if (__method == null)
      throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);

    switch (__method.intValue ())
    {
       case 0:  // WhatsTheWord/client/admin/AdminService/request
       {
         AdminRequestType type = AdminRequestTypeHelper.read (in);
         Admin admin = AdminHelper.read (in);
         ClientCallback adminCallback = ClientCallbackHelper.read (in);
         this.request (type, admin, adminCallback);
         out = $rh.createReply();
         break;
       }

       default:
         throw new org.omg.CORBA.BAD_OPERATION (0, org.omg.CORBA.CompletionStatus.COMPLETED_MAYBE);
    }

    return out;
  } // _invoke

  // Type-specific CORBA::Object operations
  private static String[] __ids = {
    "IDL:WhatsTheWord/client/admin/AdminService:1.0"};

  public String[] _all_interfaces (org.omg.PortableServer.POA poa, byte[] objectId)
  {
    return (String[])__ids.clone ();
  }

  public AdminService _this()
  {
    return AdminServiceHelper.narrow(
    super._this_object());
  }

  public AdminService _this(org.omg.CORBA.ORB orb)
  {
    return AdminServiceHelper.narrow(
    super._this_object(orb));
  }


} // class AdminServicePOA
