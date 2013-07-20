package org.carlspring.artifactgeneration;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.carlspring.artifactgeneration.ArtifactCollection;

public class ArtifactGenerator
{

    public final static long KB_FACTOR = 1024;
    public final static long MB_FACTOR = 1024 * KB_FACTOR;
    public final static long GB_FACTOR = 1024 * MB_FACTOR;

    /**
     * The number of artifacts to generate.
     */
    private long artifactsNumber = 10;

    /**
     * Maximum file size of artifact
     */
    private int maxSizePerFile = 10;

    /**
     * Should we create more than one artifact with the same?
     * I.e com.org.artifact_name-v1, com.org.artifact_name-v2, com.org.artifact_name-v3
     */
    private long artifactUniqueness = 1;

    /**
     * Maximum overall size of the repository.
     */
    private long maxOverallSize = -1;

    /**
     * Repository path
     */
    private String repositoryPath = null;


    public ArtifactGenerator(String repositoryPath)
            throws IOException
    {
        this.repositoryPath = repositoryPath;
        prepareRepositoryPath();
    }

    public ArtifactGenerator(String repositoryPath,
                             long artifacts,
                             long maxOverallSize)
            throws IOException
    {
        this.repositoryPath = repositoryPath;
        this.artifactsNumber = artifacts;
        this.maxOverallSize = maxOverallSize;

        prepareRepositoryPath();
    }

    public ArtifactGenerator(String repositoryPath,
                             long artifacts,
                             int maxSizePerFile,
                             long maxOverallSize)
            throws IOException
    {
        this.repositoryPath = repositoryPath;
        this.artifactsNumber = artifacts;
        this.maxSizePerFile = maxSizePerFile;
        this.maxOverallSize = maxOverallSize;

        prepareRepositoryPath();
    }

    /**
     * Create repository path
     *
     * @throws java.io.IOException
     */
    private void prepareRepositoryPath()
            throws IOException
    {
        // Delete old repository
        File full_path_dir = new File(this.repositoryPath);
        if (full_path_dir.exists() && full_path_dir.isDirectory())
        {
            FileUtils.forceDelete(full_path_dir);
        }

        // Create new repository
        //noinspection ResultOfMethodCallIgnored
        new File(this.repositoryPath).mkdirs();
    }

    /**
     * Generate random artifacts
     */
    public void generate()
    {
        if (this.getArtifactsNumber() < 1)
        {
            throw new Error("Please set a valid number of artifacts to generate.");
        }

        ArtifactCollection artifactCollection = new ArtifactCollection(this);
        try
        {
            artifactCollection.generateRandomCollection();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        Collection<ArrayList> artifacts = artifactCollection.getArtifacts().values();
        for (ArrayList<Artifact> artifact : artifacts)
        {
            for (int i = 0; i < artifact.size(); i++)
            {
                Artifact af = artifact.get(i);
                try
                {
                    af.save(repositoryPath);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

    public long getMaxOverallSize()
    {
        return this.maxOverallSize;
    }

    public void setMaxOverallSize(long maxOverallSize)
    {
        this.maxOverallSize = maxOverallSize;
    }

    public long getArtifactsNumber()
    {
        return this.artifactsNumber;
    }

    public void setArtifactsNumber(long artifactsNumber)
    {
        this.artifactsNumber = artifactsNumber;
    }

    public int getMaxSizePerFile()
    {
        return this.maxSizePerFile;
    }

    public void setMaxSizePerFile(int maxSizePerFile)
    {
        this.maxSizePerFile = maxSizePerFile;
    }

    public long getArtifactUniqueness()
    {
        return this.artifactUniqueness;
    }

    public void setArtifactUniqueness(long artifactUniqueness)
    {
        this.artifactUniqueness = artifactUniqueness;
    }

    public String getRepositoryPath()
    {
        return this.repositoryPath;
    }

}