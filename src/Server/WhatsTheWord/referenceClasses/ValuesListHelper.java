package Server.WhatsTheWord.referenceClasses;


/**
* WhatsTheWord/referenceClasses/ValuesListHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WhatsTheWord.idl
* Thursday, May 15, 2025 6:17:05 PM SGT
*/

abstract public class ValuesListHelper
{
  private static String  _id = "IDL:WhatsTheWord/referenceClasses/ValuesList:1.0";

  public static void insert (org.omg.CORBA.Any a, ValuesList that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static ValuesList extract (org.omg.CORBA.Any a)
  {
    return read (a.create_input_stream ());
  }

  private static org.omg.CORBA.TypeCode __typeCode = null;
  private static boolean __active = false;
  synchronized public static org.omg.CORBA.TypeCode type ()
  {
    if (__typeCode == null)
    {
      synchronized (org.omg.CORBA.TypeCode.class)
      {
        if (__typeCode == null)
        {
          if (__active)
          {
            return org.omg.CORBA.ORB.init().create_recursive_tc ( _id );
          }
          __active = true;
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [1];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_any);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_sequence_tc (0, _tcOf_members0);
          _members0[0] = new org.omg.CORBA.StructMember (
            "values",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (ValuesListHelper.id (), "ValuesList", _members0);
          __active = false;
        }
      }
    }
    return __typeCode;
  }

  public static String id ()
  {
    return _id;
  }

  public static ValuesList read (org.omg.CORBA.portable.InputStream istream)
  {
    ValuesList value = new ValuesList();
    int _len0 = istream.read_long ();
    value.values = new org.omg.CORBA.Any[_len0];
    for (int _o1 = 0;_o1 < value.values.length; ++_o1)
      value.values[_o1] = istream.read_any ();
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, ValuesList value)
  {
    ostream.write_long (value.values.length);
    for (int _i0 = 0;_i0 < value.values.length; ++_i0)
      ostream.write_any (value.values[_i0]);
  }

}
