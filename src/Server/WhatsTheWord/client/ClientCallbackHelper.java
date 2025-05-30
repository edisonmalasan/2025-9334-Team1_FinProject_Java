package Server.WhatsTheWord.client;


/**
* WhatsTheWord/client/ClientCallbackHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WhatsTheWord.idl
* Thursday, May 15, 2025 6:17:05 PM SGT
*/

abstract public class ClientCallbackHelper
{
  private static String  _id = "IDL:WhatsTheWord/client/ClientCallback:1.0";

  public static void insert (org.omg.CORBA.Any a, ClientCallback that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static ClientCallback extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      __typeCode = org.omg.CORBA.ORB.init ().create_interface_tc (ClientCallbackHelper.id (), "ClientCallback");
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static ClientCallback read (org.omg.CORBA.portable.InputStream istream)
  {
    return narrow (istream.read_Object (_ClientCallbackStub.class));
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, ClientCallback value)
  {
    ostream.write_Object ((org.omg.CORBA.Object) value);
  }

  public static ClientCallback narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof ClientCallback)
      return (ClientCallback)obj;
    else if (!obj._is_a (id ()))
      throw new org.omg.CORBA.BAD_PARAM ();
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      _ClientCallbackStub stub = new _ClientCallbackStub();
      stub._set_delegate(delegate);
      return stub;
    }
  }

  public static ClientCallback unchecked_narrow (org.omg.CORBA.Object obj)
  {
    if (obj == null)
      return null;
    else if (obj instanceof ClientCallback)
      return (ClientCallback)obj;
    else
    {
      org.omg.CORBA.portable.Delegate delegate = ((org.omg.CORBA.portable.ObjectImpl)obj)._get_delegate ();
      _ClientCallbackStub stub = new _ClientCallbackStub();
      stub._set_delegate(delegate);
      return stub;
    }
  }

}
