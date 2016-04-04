package org.composi;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GitFiles {
    private String workingDirectory;
    private Repository repository;
    GitFiles(String workingDirectory){
        this.workingDirectory = workingDirectory;
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        try {
            this.repository = builder.setGitDir(new File(workingDirectory))
                    .readEnvironment()
                    .findGitDir()
                    .build();
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    }

    /**
     * Function which will walk the latest commit to return all pdf files in the repository.
     * @return returns a List<String> of files in the git repository from the "HEAD" commit. Will filter out any files not ending with PDF.
     *
     */
    List<String> getFiles(){
        List<String> list = new ArrayList<String>();
        try {
            Map<String,Ref> map = repository.getAllRefs();
            Ref head = map.get("HEAD");
            RevWalk walk = new RevWalk(repository);
            RevCommit commit = walk.parseCommit(head.getObjectId());
            RevTree tree = commit.getTree();
            TreeWalk treeWalk = new TreeWalk(repository);
            treeWalk.addTree(tree);
            treeWalk.setRecursive(true);
            list = new ArrayList<String>();
            while (treeWalk.next()){
                String path = treeWalk.getPathString();
                if (path.split("\\.")[path.split("\\.").length -1 ].equals("pdf")){
                    list.add(treeWalk.getPathString());
                }
            }
        }catch (IOException e ){
            System.out.println("No HEAD commit could be found.");
        }
        return list;
    }
}
