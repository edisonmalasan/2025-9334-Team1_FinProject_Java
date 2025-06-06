package Server.WhatsTheWord.referenceClasses;


/**
* WhatsTheWord/referenceClasses/GameLobbyHelper.java .
* Generated by the IDL-to-Java compiler (portable), version "3.2"
* from WhatsTheWord.idl
* Thursday, May 15, 2025 6:17:05 PM SGT
*/

abstract public class GameLobbyHelper
{
  private static String  _id = "IDL:WhatsTheWord/referenceClasses/GameLobby:1.0";

  public static void insert (org.omg.CORBA.Any a, GameLobby that)
  {
    org.omg.CORBA.portable.OutputStream out = a.create_output_stream ();
    a.type (type ());
    write (out, that);
    a.read_value (out.create_input_stream (), type ());
  }

  public static GameLobby extract (org.omg.CORBA.Any a)
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
          org.omg.CORBA.StructMember[] _members0 = new org.omg.CORBA.StructMember [6];
          org.omg.CORBA.TypeCode _tcOf_members0 = null;
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[0] = new org.omg.CORBA.StructMember (
            "gameId",
            _tcOf_members0,
            null);
          _tcOf_members0 = PlayerHelper.type ();
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_sequence_tc (0, _tcOf_members0);
          _members0[1] = new org.omg.CORBA.StructMember (
            "players",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[2] = new org.omg.CORBA.StructMember (
            "waitingTime",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().get_primitive_tc (org.omg.CORBA.TCKind.tk_long);
          _members0[3] = new org.omg.CORBA.StructMember (
            "gameTime",
            _tcOf_members0,
            null);
          _tcOf_members0 = org.omg.CORBA.ORB.init ().create_string_tc (0);
          _members0[4] = new org.omg.CORBA.StructMember (
            "word",
            _tcOf_members0,
            null);
          _tcOf_members0 = PlayerHelper.type ();
          _members0[5] = new org.omg.CORBA.StructMember (
            "winner",
            _tcOf_members0,
            null);
          __typeCode = org.omg.CORBA.ORB.init ().create_struct_tc (GameLobbyHelper.id (), "GameLobby", _members0);
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

  public static GameLobby read (org.omg.CORBA.portable.InputStream istream)
  {
    GameLobby value = new GameLobby();
    value.gameId = istream.read_long ();
    int _len0 = istream.read_long ();
    value.players = new Player[_len0];
    for (int _o1 = 0;_o1 < value.players.length; ++_o1)
      value.players[_o1] = PlayerHelper.read (istream);
    value.waitingTime = istream.read_long ();
    value.gameTime = istream.read_long ();
    value.word = istream.read_string ();
    value.winner = PlayerHelper.read (istream);
    return value;
  }

  public static void write (org.omg.CORBA.portable.OutputStream ostream, GameLobby value)
  {
    ostream.write_long (value.gameId);
    ostream.write_long (value.players.length);
    for (int _i0 = 0;_i0 < value.players.length; ++_i0)
      PlayerHelper.write (ostream, value.players[_i0]);
    ostream.write_long (value.waitingTime);
    ostream.write_long (value.gameTime);
    ostream.write_string (value.word);
    PlayerHelper.write (ostream, value.winner);
  }

}
