package com.oxchains.themis.repo.util;

import org.hibernate.LobHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author ccl
 * @time 2017-11-08 13:55
 * @name HibernateBlobUtil
 * @desc:
 */
public class HibernateBlobUtil {
    private HibernateBlobUtil(){}

    public static java.sql.Blob ObjectToBlob(Object obj)  {

            Configuration cfg = new Configuration().configure();
            ServiceRegistry sr = new StandardServiceRegistryBuilder().applySettings(cfg.getProperties()).build();
            SessionFactory sf = cfg.buildSessionFactory(sr);
            Session session = sf.openSession();
            LobHelper lobHelper = session.getLobHelper();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(out);
            outputStream.writeObject(obj);
            byte[] bytes = out.toByteArray();
            outputStream.close();
            //return Hibernate.createBlob(bytes);
            return lobHelper.createBlob(bytes);
        } catch (Exception e) {
            return null;
        }

    }

    public static Object BlobToObject(java.sql.Blob desblob)   {
        try {
            Object obj = null;
            ObjectInputStream in = new ObjectInputStream(
                    desblob.getBinaryStream());
            obj = in.readObject();
            in.close();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
