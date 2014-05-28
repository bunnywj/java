package Matrix;

import java.util.Scanner;

public class MatrixTesting {
	
	public static Matrix init(){
		Scanner input = new Scanner(System.in);
		System.out.println("Input row number of matrix: ");
		int row = input.nextInt();
		System.out.println("Input col number of matrix: ");
		int col = input.nextInt();
		System.out.println("Enter matrix: ");
		Matrix mat = new Matrix(row, col);
		for (int i = 0; i < mat.length(); ++i) {
			double val = input.nextDouble();
			mat.set(i, val);
		}
		return mat;
	}
	
	public static void main(String[] args) {
		Matrix A = init();
		Matrix B = init();
		
		//Matrix C = A.addMatrix(B);
		//C.display();
		//Matrix D = A.subMatrix(B);
		//D.display();
		Matrix E = A.mulMatrix(B);
		E.display();
		
	}
}
