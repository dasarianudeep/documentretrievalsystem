package edu.neu.mainclasses;

public class Result {
	
	private int queryid;
	
	private String filename;
	
	private int rank;
	
	private double bm25score;
	
	public Result()
	{
		
	}

	public int getQueryid() {
		return queryid;
	}

	public void setQueryid(int queryid) {
		this.queryid = queryid;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public double getBm25score() {
		return bm25score;
	}

	public void setBm25score(double bm25score) {
		this.bm25score = bm25score;
	}
	
	

}
