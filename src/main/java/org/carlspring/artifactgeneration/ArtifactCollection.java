package org.carlspring.artifactgeneration;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class ArtifactCollection
{

    private Map<String, ArrayList<Artifact>> artifacts = new HashMap<>();

    private String[] groupIds = new String[]{ "com.foo",
                                              "com.bar",
                                              "org.testing",
                                              "org.oss" };

    private String[] classifiers = new String[]{ "javadoc",
                                                 "jdk17",
                                                 "sources",
                                                 "test",
                                                 "win32" };

    private ArtifactGenerator generator;

    private Random random = new Random();


    public ArtifactCollection(ArtifactGenerator generator)
    {
        this.generator = generator;
    }

    /**
     * Generate an artifact collection
     *
     * @throws Exception
     */
    public void generateRandomCollection()
            throws Exception
    {
        if (generator.getArtifactsNumber() > 0)
        {
            int uniqueness = randomInt((int) generator.getArtifactUniqueness());
            long maxSize = generator.getMaxOverallSize();

            String artifactId = randomArtifactId();
            String groupId = randomGroupId();
            String classifier = randomClassifier();
            int version = 1;
            int size = randomSize();

            for (int i = 0; i < generator.getArtifactsNumber(); i++)
            {
                Artifact artifact = new Artifact();

                if (uniqueness < generator.getArtifactUniqueness())
                {
                    version = version + 1;
                    classifier = randomClassifier();
                    uniqueness++;
                }
                else
                {
                    uniqueness = randomInt((int) generator.getArtifactUniqueness());
                    version = 1;
                    artifactId = randomArtifactId();
                    groupId = randomGroupId();
                    classifier = randomClassifier();
                }

                artifact.setArtifactId(artifactId);
                artifact.setGroupId(groupId);
                artifact.setVersion(String.valueOf(version));
                artifact.setClassifier(classifier);
                artifact.setSize(size);

                if (!this.artifacts.containsKey(artifactId))
                {
                    this.artifacts.put(artifactId, new ArrayList<Artifact>());
                }

                this.artifacts.get(artifactId).add(artifact);
            }
        }
        else if (generator.getArtifactsNumber() < 0 && generator.getMaxOverallSize() > 0)
        {
            // TODO
        }
        else
        {
            throw new Exception("Exception");
        }
    }

    public Map getArtifacts()
    {
        return this.artifacts;
    }

    private String randomArtifactId()
    {
        return RandomStringUtils.randomAlphabetic(12);
    }

    private String randomGroupId()
    {
        return this.groupIds[random.nextInt(this.groupIds.length)];
    }

    private String randomClassifier()
    {
        int rand = random.nextInt(10000);
        if (rand < 8000)
        {
            return this.classifiers[random.nextInt(this.classifiers.length)];
        }
        else
        {
            return null;
        }
    }

    private int randomSize()
    {
        return random.nextInt(generator.getMaxSizePerFile());
    }

    private int randomInt(int max)
    {
        return randomInt(1, max);
    }

    private int randomInt(int min,
                          int max)
    {
        Random rn = new Random();
        if (max % 2 == 1) max = max + 1;
        if (min % 2 == 0) min = min - 1;
        int range = (max - min + 1) / 2;
        return rn.nextInt(range) * 2 + min;
    }

}
