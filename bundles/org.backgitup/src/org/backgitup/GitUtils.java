package org.backgitup;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeResult;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.RebaseResult;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.errors.NoWorkTreeException;
import org.eclipse.jgit.transport.PushResult;

final class GitUtils {

	private GitUtils() {
		// no-op
	}

	static void pull(Git git) {
		try {
			PullResult pullResult = git.pull().call();
			System.out.println(pullResult);
			MergeResult mergeResult = pullResult.getMergeResult();
			if (mergeResult != null) {
				// do something
			}
			RebaseResult rebaseResult = pullResult.getRebaseResult();
			if (rebaseResult != null) {
				// do something
			}
		} catch (WrongRepositoryStateException e) {
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			e.printStackTrace();
		} catch (DetachedHeadException e) {
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		} catch (CanceledException e) {
			e.printStackTrace();
		} catch (RefNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static void push(Git git) {
		try {
			Iterable<PushResult> pushResults = git.push().setPushAll().call();
			for (PushResult pushResult : pushResults) {
				System.out.println(pushResult.getMessages());
			}
		} catch (JGitInternalException e) {
			e.printStackTrace();
		} catch (InvalidRemoteException e) {
			e.printStackTrace();
		}
	}

	static void addAll(Git git) {
		// commit new changes if it has some
		try {
			Status status = git.status().call();
			if (needAddAll(status)) {
				DirCache cache = null;
				try {
					cache = git.add().addFilepattern(".").call();
				} catch (NoFilepatternException e) {
					e.printStackTrace();
				}

				try {
					if (cache != null && cache.getEntryCount() > 0) {
						git.commit()
								.setMessage(
										"Resynch watched folder with the repository")
								.call();
					}
				} catch (NoHeadException e) {
					e.printStackTrace();
				} catch (NoMessageException e) {
					e.printStackTrace();
				} catch (ConcurrentRefUpdateException e) {
					e.printStackTrace();
				} catch (JGitInternalException e) {
					e.printStackTrace();
				} catch (WrongRepositoryStateException e) {
					e.printStackTrace();
				}
			}
		} catch (NoWorkTreeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	static Git initOrOpenGit(Path dir) {
		final File gitRepoFile = dir.toFile();
		Git git = null;
		try {
			git = Git.open(gitRepoFile);
			System.out.println("Git repo opened");
		} catch (IOException e) {
			git = Git.init().setBare(false).setDirectory(gitRepoFile).call();
			System.out.println("Git repo inited");
		}
		return git;
	}

	static boolean needAddAll(Status status) {
		return !status.getAdded().isEmpty() 
				|| !status.getChanged().isEmpty()
				|| !status.getMissing().isEmpty()
				|| !status.getModified().isEmpty()
				|| !status.getRemoved().isEmpty()
				|| !status.getUntracked().isEmpty();
	}

	static boolean hasConflict(Status status) {
		return !status.getConflicting().isEmpty();
	}
}
