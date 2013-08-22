package net.minecraft.client.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@SideOnly(Side.CLIENT)
public class FileResourcePack extends AbstractResourcePack implements Closeable
{
    public static final Splitter field_110601_c = Splitter.on('/').omitEmptyStrings().limit(3);
    private ZipFile field_110600_d;

    public FileResourcePack(File par1File)
    {
        super(par1File);
    }

    private ZipFile func_110599_c() throws IOException
    {
        if (this.field_110600_d == null)
        {
            this.field_110600_d = new ZipFile(this.field_110597_b);
        }

        return this.field_110600_d;
    }

    protected InputStream func_110591_a(String par1Str) throws IOException
    {
        ZipFile zipfile = this.func_110599_c();
        ZipEntry zipentry = zipfile.getEntry(par1Str);

        if (zipentry == null)
        {
            throw new ResourcePackFileNotFoundException(this.field_110597_b, par1Str);
        }
        else
        {
            return zipfile.getInputStream(zipentry);
        }
    }

    public boolean func_110593_b(String par1Str)
    {
        try
        {
            return this.func_110599_c().getEntry(par1Str) != null;
        }
        catch (IOException ioexception)
        {
            return false;
        }
    }

    public Set func_110587_b()
    {
        ZipFile zipfile;

        try
        {
            zipfile = this.func_110599_c();
        }
        catch (IOException ioexception)
        {
            return Collections.emptySet();
        }

        Enumeration enumeration = zipfile.entries();
        HashSet hashset = Sets.newHashSet();

        while (enumeration.hasMoreElements())
        {
            ZipEntry zipentry = (ZipEntry)enumeration.nextElement();
            String s = zipentry.getName();

            if (s.startsWith("assets/"))
            {
                ArrayList arraylist = Lists.newArrayList(field_110601_c.split(s));

                if (arraylist.size() > 1)
                {
                    String s1 = (String)arraylist.get(1);

                    if (!s1.equals(s1.toLowerCase()))
                    {
                        this.func_110594_c(s1);
                    }
                    else
                    {
                        hashset.add(s1);
                    }
                }
            }
        }

        return hashset;
    }

    protected void finalize()
    {
        this.close();

        try
        {
            super.finalize();
        }
        catch (Throwable t)
        {
        }
    }

    public void close()
    {
        if (this.field_110600_d != null)
        {
            try
            {
                this.field_110600_d.close();
            }
            catch (Exception ex)
            {
            }

            this.field_110600_d = null;
        }
    }
}
