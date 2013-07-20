package org.carlspring.artifactgeneration;

import java.io.IOException;

import org.junit.Test;

public class ManagerTest
{


    @Test
    public void testGeneration()
    {
        ArtifactGenerator ag = null;
        try
        {
            //ag = new ArtifactGenerator(System.getProperty("java.io.tmpdir"));
            String path = "target/test-repository";

            ag = new ArtifactGenerator(path);
            ag.setArtifactsNumber(10);
            ag.setMaxOverallSize(15);
            ag.setMaxSizePerFile(3);
            ag.setArtifactUniqueness(10);
            ag.generate();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
