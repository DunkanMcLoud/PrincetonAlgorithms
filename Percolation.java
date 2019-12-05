import edu.princeton.cs.algs4.WeightedQuickUnionUF;

import java.util.Scanner;

public class Percolation {
    private boolean[] grid;
    private WeightedQuickUnionUF connections;
    private int step;
    private int len;
    private int edge_ind;
    private int numOfOpennedSites;

    // creates n-by-n grid, with all sites initially blocked as well as relation array
    // array of  len (n x n)
    // site is one of : True - open site
    //                ; False - closed site
    //  ex: Percolation(3)
    ///  ex : [   0- top
    //         1  2  3
    //         4  5  6
    //         7  8  9 -
    //           10 -bottom
    //         ]

    //  API of Percolation picks elements in this coordinate-system:
    /// [ 0.0 0.1 0.2]
    /// [ 1.0 1.1 1.2]
    /// [ 2.0 2.1 2.2]
    public Percolation(int n) throws IllegalArgumentException {
        if (n < 0) {
            throw new IllegalArgumentException();
        } else {
            step = n;     // step for determining adjacent elements
            len = n * n + 2;  // length for defining relation array and experimental grid
            edge_ind=n*n+1;
            grid = new boolean[len];
            grid[0]=true;
            grid[edge_ind]=true;
            connections = new WeightedQuickUnionUF(len);
            numOfOpennedSites=0;
            }
        }

        // opens the site (row, col) if it is not open already
        public void open(int row, int col) throws IllegalArgumentException {
            if (row < 0 || col < 0) {
                throw new IllegalArgumentException();
            } else {
                if (!isOpen(row, col)) {
                    int i = xyto1D(row, col);
                    grid[i]=true;
                numOfOpennedSites++;
                integrate(row,col);
            }
        }}


        //integrates openned element in a scheme;
        //connects with current set all adjacent openned groups
        private void integrate(int row, int col) {
        upJoin(row,col);
        leftJoin(row,col);
        rightJoin(row,col);
        downJoin(row,col);
    }

    //takes coordinates of openned element and adds it to group of openned neighours(root if it is present) and fills if
    // element up is filled
    private void upJoin(int row, int col) {
        int ind_this = xyto1D(row, col); //index of this element
        if (row==0){ // if top row -> connect and fill
            connections.union(ind_this,0);
        }else{
            int ind_top = xyto1D(row-1,col);
            boolean isTopOpen = isOpen(row-1,col);
            if (isTopOpen){
                    connections.union(ind_this,ind_top);
                }
            }}


    //takes coordinates of openned element and adds it to group of openned neighours(root if it is present) and
    // fills up downward neighbours if current element isFilled(+).
    private void downJoin(int row, int col) {
        int ind_this = xyto1D(row,col);
        if (row==step-1){ // if it is the bottom edge
                connections.union(ind_this,edge_ind);
            }
        else{
            int ind_down = xyto1D(row+1,col);
            boolean isDownOpen = isOpen(row+1,col);
            if (isDownOpen){
                    connections.union(ind_down,ind_this);
                }
        }
        }
    //  ex: Percolation(3)
    ///  ex : [   0- top
    //         1  2  3
    //         4  5  6
    //         7  8  9 -
    //           10 -bottom
    //         ]  - internal data representation

    //  API of Percolation picks elements in this coordinate-system:
    /// [ 0.0 0.1 0.2]
    /// [ 1.0 1.1 1.2]
    /// [ 2.0 2.1 2.2]
    private void rightJoin(int row, int col) {
        if (col==step-1) return;
        else {
            int ind_this = xyto1D(row, col);
            int ind_right = xyto1D(row,col+1);
            if (isOpen(row, col+1)){
                connections.union(ind_this,ind_right);
            }
        }
    }

    private void leftJoin(int row, int col) {
    if (col==0) return;
    else {
        int ind_this = xyto1D(row, col);
        int ind_left = xyto1D(row,col-1);
        if (isOpen(row,col-1)){
            connections.union(ind_this,ind_left);
        }
    }
    }





    // Transforms x and y coordinates to 1D system / row*step-(step-col)
    //RETURNS : index of given element in 1D Array
    private int xyto1D ( int row, int col){
            if (row==0){
                return 1+col;
            }
            else{
                return step*row+(1+col);
            }
        }


        private int getStep(){
        return step;
        }


        // is the site (row, col) open?
        public boolean isOpen ( int row, int col) throws IllegalArgumentException {
            if (row < 0|| col < 0) {
                throw new IllegalArgumentException();
            } else {
                int i = xyto1D(row, col);
                return grid[i];
            }
        }

        // is the site (row, col) full?
        // site is full if its root in connections is 0 !
        public boolean isFull ( int row, int col)throws IllegalArgumentException {
        if (row<0 || col<0){throw new IllegalArgumentException("Mistake in isFull()");}
        else {
            int i = xyto1D(row, col);
            return connections.connected(i,0);
        }
    }

        // returns the number of open sites
        public int numberOfOpenSites () {
            return numOfOpennedSites;
        }

        // does the system percolate?
        //checks if last element of filled relations has a root of zero
        public boolean percolates () {
            return connections.connected(edge_ind,0);
        }

        // test client (optional)
        public static void main (String[]args){
            System.out.println("Enter a number");
            Scanner scan = new Scanner(System.in);
            int n = scan.nextInt();
            Percolation tested = new Percolation(n);   // Constructor test
            for (int i = 0; i < (tested.edge_ind); i++) {
                System.out.println(tested.grid[i]);
            }


            System.out.println("f   " + tested.grid[0]);
            System.out.println("f   " + tested.grid[n - 1]);

            System.out.println("Test xyto1D #1"+ tested.isOpen(0,0));
            tested.open(0,0);
            System.out.println(tested.grid[1]);
            System.out.println("test ifFull? "+ tested.isFull(0,0));
            System.out.println("Test xyto1D #1 ------>"+ tested.isOpen(0,0));


            System.out.println("Test xyto1D #2"+ tested.isOpen(1,0));
            tested.open(1,0);
            System.out.println("print xyto1D     "+ tested.xyto1D(1,0));
            System.out.println(tested.grid[tested.xyto1D(1,0)]);
            System.out.println("test ifFull? "+ tested.isFull(1,0));
            System.out.println("Test xyto1D #2 ------>"+ tested.isOpen(1,0));


            System.out.println("not opened   " + tested.connections.find(1)); // test for open first line()
            tested.open(0, 0);
            System.out.println("must be opened " + tested.connections.find(1));

            System.out.println(tested.xyto1D(10,9));

        }

    }
