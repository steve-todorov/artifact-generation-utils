package org.carlspring.artifactgeneration;

import org.carlspring.artifactgeneration.io.RandomInputStream;
import org.carlspring.artifactgeneration.io.RandomOutputStream;
import org.carlspring.maven.commons.model.ModelWriter;
import org.carlspring.maven.commons.util.ChecksumUtils;

import java.io.File;
import java.io.IOException;

import org.apache.maven.model.Developer;
import org.apache.maven.model.Model;

public class Artifact
        implements Cloneable
{

    private String groupId = "com.foo";
    private String groupPath = "com" + File.separator + "foo";
    private String artifactId = "artifact_name";
    private String version = "1";
    private String classifier = null; // javadoc, jdk17, sources, test and win32
    private String type = "jar";
    private Integer size = null;

    public Artifact()
    {

    }

    public Artifact(String groupId,
                    String artifactId,
                    Integer size)
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.size = size;
    }

    public Artifact(String groupId,
                    String artifactId,
                    String version,
                    Integer size)
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.size = size;
    }

    /**
     * Save the artifact
     *
     * @param repositoryPath path to the repository
     * @param size           file size in MB
     */
    public void save(String repositoryPath,
                     Integer size)
            throws Exception
    {
        this.size = size;
        save(repositoryPath);
    }

    /**
     * Save the artifact
     *
     * @param repositoryPath path to the repository
     */
    public void save(String repositoryPath)
            throws Exception
    {
        System.out.println("repository: " + repositoryPath);
        System.out.println("groupId:    " + this.groupPath);
        System.out.println("artifactId: " + this.artifactId);
        System.out.println("version: "    + this.version);

        prepareArtifactPath(repositoryPath + File.separator + this.groupPath);

        String file = this.groupPath + File.separator + this.artifactId + "-" + this.version;
        if (this.classifier != null)
        {
            file += "-" + this.classifier;
        }

        String jarFile = repositoryPath + File.separator + file + "." + this.type;
        String pomFile = repositoryPath + File.separator + file + ".pom";

        System.out.println("FILE: " + file);

        RandomInputStream inputStream = new RandomInputStream(this.size * ArtifactGenerator.MB_FACTOR);
        RandomOutputStream outputStream = new RandomOutputStream(jarFile);
        int byteRead;
        while ((byteRead = inputStream.read()) != -1)
        {
            outputStream.write(byteRead);
        }

        File pom = new File(pomFile);
        pom.createNewFile();
        Developer dev = new Developer();
        dev.setName("Application " + this.artifactId);

        Model model = new Model();
        model.addDeveloper(dev);

        ModelWriter modelWriter = new ModelWriter(model, pom);
        modelWriter.write();

        ChecksumUtils.generateMD5ChecksumFile(jarFile);
        ChecksumUtils.generateSHA1ChecksumFile(jarFile);

        ChecksumUtils.generateMD5ChecksumFile(pomFile);
        ChecksumUtils.generateSHA1ChecksumFile(pomFile);

    }

    private void prepareArtifactPath(String repositoryPath)
            throws IOException
    {
        // Delete old repository
        File full_path_dir = new File(repositoryPath);
        if (!full_path_dir.exists())
        {
            full_path_dir.mkdirs();
        }
    }

    public String getGroupId()
    {
        return groupId;
    }

    public String getGroupPath()
    {
        return groupPath;
    }

    public void setGroupId(String groupId)
    {
        this.groupId = groupId;
        this.groupPath =
                groupId.replace(".", File.separator) + File.separator + this.artifactId + File.separator + this.version;
    }

    public String getArtifactId()
    {
        return artifactId;
    }

    public void setArtifactId(String artifactId)
    {
        this.artifactId = artifactId.toLowerCase();
        this.groupPath =
                groupId.replace(".", File.separator) + File.separator + this.artifactId + File.separator + this.version;
    }

    public String getVersion()
    {
        return version;
    }

    public void setVersion(String version)
    {
        this.version = version;
        this.groupPath =
                groupId.replace(".", File.separator) + File.separator + this.artifactId + File.separator + this.version;
    }

    public String getClassifier()
    {
        return classifier;
    }

    public void setClassifier(String classifier)
    {
        this.classifier = classifier;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public Integer getSize()
    {
        return size;
    }

    /**
     * @param size in MB
     */
    public void setSize(int size)
    {
        this.size = size;
    }
}
