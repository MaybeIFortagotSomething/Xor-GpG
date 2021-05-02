package fr.dinasty.gpgxor;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Random;

public class Main {
    private static final String[] flags ={"foihuhahujklsljp","haehlepuhapuhrel"};
    private static final int NB = 16;
    public static void main(String[] args)
    {
        /*
        ============== File generation: Flag File ==============
        */
        Random random = new Random();
        int first = random.nextInt(NB);
        int second = random.nextInt(NB);
        while (second==first)
        {
            second = random.nextInt(NB);
        }
        if(second<first)
        {
            int tmp = second;
            second = first;
            first = tmp;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i<NB; i++)
        {
            if(i == first) {
                stringBuilder.append(flags[0]).append("\n");
                continue;
            }
            if(i == second)
            {
                stringBuilder.append(flags[1]).append("\n");
                continue;
            }
            stringBuilder.append(fakeString(NB)).append("\n");
        }
        String flagString = xorg(stringBuilder.toString());
        System.out.println("First -> " +first +"\nSecond -> "+second);
        /*
        ============== File generation: GPG KEY File ==============
        */
        stringBuilder = new StringBuilder();
        File publicKey = new File("publicKey1.key");
        for(int i = 0; i<NB; i++) {
            if(i == first || i==second) {
                stringBuilder.append(loadContent(publicKey)).append("\n\n\n");
                continue;
            }
            for(int j=0;j<116;j++)
            {
                stringBuilder.append(fakeString(64)).append("\n");
            }
            stringBuilder.append(fakeString(37)).append("\n");
            stringBuilder.append("=").append(fakeString(4)).append("\n\n\n");
        }
        String keyString = stringBuilder.toString();
        System.out.println(loadContent(publicKey).length());
        save(new File("/home/dinasty/Work/Java/Xor-GpG/encKey.enc"), keyString);
        save(new File("/home/dinasty/Work/Java/Xor-GpG/encFlags.enc"), flagString);
    }
    private static String xorg(String data)
    {
        byte[] bytes = data.getBytes(StandardCharsets.US_ASCII);
        byte[] newBytes = new byte[bytes.length];
        for (int i=0; i<bytes.length; i++)
        {
            newBytes[i] = (byte) (127-bytes[i]);
        }
        return new String(newBytes);
    }
    private static String fakeString(int len)
    {
        //42 -> 122
        Random random = new Random();
        byte[] data = new byte[len];
        for(int i=0;i<len;i++)
        {
            data[i]=(byte)(random.nextInt(81)+42);
        }
        return new String(data);
    }

    private static void createFile(File file) throws IOException
    {
        if(!file.exists()){
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
    }

    private static void save(File file, String text)
    {
        final FileWriter fileWriter;

        try
        {
            createFile(file);
            fileWriter = new FileWriter(file);
            fileWriter.write(text);
            fileWriter.flush();
            fileWriter.close();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
    }

    private static String loadContent(File file)
    {
        if(!file.exists())
        {
            return "";
        }

        try
        {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuilder stringBuilder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            reader.close();
            return stringBuilder.toString();
        }
        catch (IOException exception)
        {
            exception.printStackTrace();
        }
        return "";
    }
}
