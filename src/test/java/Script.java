import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.Driver;
import org.h2.util.JdbcUtils;
import org.h2.util.StringUtils;
import org.h2.util.Tool;

public class Script extends Tool
{
    public static void main(String[] args)
            throws SQLException
    {
        String[] arr= {"-url","jdbc:h2:D:/Java/H2Test/iotDb","-user","root","-password","root","-script","test.zip","-options","compression","zip"};

        new Script().runTool(arr);
    }

    public void runTool(String[] paramArrayOfString) throws SQLException
    {
        String str1 = null;
        String str2 = "";
        String str3 = "";
        String str4 = "backup.sql";
        String str5 = "";
        String str6 = "";
        for (int i = 0; (paramArrayOfString != null) && (i < paramArrayOfString.length); i++) {
            String str7 = paramArrayOfString[i];
            if (str7.equals("-url")) {
                str1 = paramArrayOfString[(++i)];
            } else if (str7.equals("-user")) {
                str2 = paramArrayOfString[(++i)];
            } else if (str7.equals("-password")) {
                str3 = paramArrayOfString[(++i)];
            } else if (str7.equals("-script")) {
                str4 = paramArrayOfString[(++i)];
            } else if (str7.equals("-options")) {
                StringBuilder localStringBuilder1 = new StringBuilder();
                StringBuilder localStringBuilder2 = new StringBuilder();
                i++;
                for (; i < paramArrayOfString.length; i++) {
                    String str8 = paramArrayOfString[i];
                    String str9 = StringUtils.toUpperEnglish(str8);
                    if (("SIMPLE".equals(str9)) || (str9.startsWith("NO")) || ("DROP".equals(str9))) {
                        localStringBuilder1.append(' ');
                        localStringBuilder1.append(paramArrayOfString[i]);
                    } else if ("BLOCKSIZE".equals(str9)) {
                        localStringBuilder1.append(' ');
                        localStringBuilder1.append(paramArrayOfString[i]);
                        i++;
                        localStringBuilder1.append(' ');
                        localStringBuilder1.append(paramArrayOfString[i]);
                    } else {
                        localStringBuilder2.append(' ');
                        localStringBuilder2.append(paramArrayOfString[i]);
                    }
                }
                str5 = localStringBuilder1.toString();
                str6 = localStringBuilder2.toString();
            } else {
                if ((str7.equals("-help")) || (str7.equals("-?"))) {
                    showUsage();
                    return;
                }
                showUsageAndThrowUnsupportedOption(str7);
            }
        }
        if (str1 == null) {
            showUsage();
            throw new SQLException("URL not set");
        }
        process(str1, str2, str3, str4, str5, str6);
    }

    public static void process(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6)
            throws SQLException
    {
        Connection localConnection = null;
        try {
            Driver.load();
            localConnection = DriverManager.getConnection(paramString1, paramString2, paramString3);
            process(localConnection, paramString4, paramString5, paramString6);
        } finally {
            JdbcUtils.closeSilently(localConnection);
        }
    }

    public static void process(Connection paramConnection, String paramString1, String paramString2, String paramString3)
            throws SQLException
    {
        Statement localStatement = paramConnection.createStatement(); Object localObject1 = null;
        try { String str = new StringBuilder().append("SCRIPT ").append(paramString2).append(" TO '").append(paramString1).append("' ").append(paramString3).toString();
            localStatement.execute(str);
        }
        catch (Throwable localThrowable2)
        {
            localObject1 = localThrowable2; throw localThrowable2;
        }
        finally {
            if (localStatement != null)
                if (localObject1 != null)
                try {
                    localStatement.close();
                } catch (Throwable localThrowable3) {
                    //localObject1.addSuppressed(localThrowable3);
                } else localStatement.close();
        }
    }
}
