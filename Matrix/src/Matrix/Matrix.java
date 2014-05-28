package Matrix;

public class Matrix {

	private int nRow;
	private int nCol;
	private double[] matrix;

	/*
	 * ���캯��
	 */
	Matrix() {
	};

	Matrix(int row, int col) {
		nRow = row;
		nCol = col;
		matrix = new double[nRow * nCol];
	}

	Matrix(double[] matrix, int row, int col) {
		nRow = row;
		nCol = col;
		matrix = matrix.clone();
	}

	/*
	 * �����ͷſռ�
	 */
	void release() {
		matrix = null;
	}

	/* 
	 * ȡ�ڣ�i,j����Ԫ��
	 */
	double get(int i, int j) {

		return matrix[i * nCol + j];
	}

	/* 
	 * ������󳤶�
	 */
	int length() {
		return nRow * nCol;
	}

	/* 
	 * ����ڣ�i��j����Ԫ��
	 */
	void set(int idx, double val) {
		matrix[idx] = val;
	}
	
	/*
	 * ��ʾ����
	 */
	void display() {
		for (int i = 0; i < nRow * nCol; ++i) {
			if (i != 0 && i % nCol == 0) {
				System.out.println();
			}
			System.out.print(matrix[i] + "  ");
		}
	}

	/*
	 * �������
	 */
	Matrix addMatrix(Matrix other) {
		if (nRow != other.nRow && nCol != other.nCol) {
			System.out.println("Two matrices can't add!");
			System.exit(1);
		}

		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] + other.matrix[i];
		}

		return result;
	}

	/* 
	 * �������
	 */
	Matrix subMatrix(Matrix other) {
		if (nRow != other.nRow && nCol != other.nCol) {
			System.out.println("Two matrices can't substract!");
			System.exit(1);
		}

		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] - other.matrix[i];
		}

		return result;
	}

	/*
	 *  �������
	 */
	Matrix mulMatrix(Matrix other) {
		if (nCol != other.nRow) {
			System.out.println("Two matrices can't multiplay!");
			System.exit(1);
		}

		Matrix result = new Matrix(nRow, other.nCol);
		for (int i = 0; i < result.nRow; ++i) {
			for (int j = 0; j < other.nCol; ++j) {
				int idx = i * other.nCol + j;
				result.matrix[idx] = 0.0;
				for (int k = 0; k < nCol; ++k) {
					// result.matrix[i] += matrix[i * nCol + j] * other.matrix[k
					// * other.nCol + i];
					result.matrix[idx] += matrix[i * nCol + k]
							* other.get(k, j);
				}
			}
		}
		return result;
	}

	/*
	 * ������ʵ�����
	 */
	Matrix addReal(double val) {
		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] + val;
		}
		return result;
	}

	/*
	 * ������ʵ�����
	 */
	Matrix subReal(double val) {
		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] - val;
		}
		return result;
	}

	/*
	 * ������ʵ�����
	 */
	Matrix mulReal(double val) {
		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] * val;
		}
		return result;
	}

	/*
	 *  ������ʵ�����
	 */
	Matrix divReal(double val) {
		if ((double) 0 == val) {
			System.out.println("0 is not divistor");
			System.exit(1);
		}

		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] / val;
		}
		return result;
	}

	/*
	 * ��λ����
	 */
	void identityMatrix() {
		if (nRow != nCol) {
			System.out
					.println("This matrix can not convert to identity matrix.");
			System.exit(1);
		}

		for (int i = 0; i < nRow; ++i) {
			for (int j = 0; j < nCol; ++j) {
				int idx = i * nCol + j;
				matrix[idx] = (i == j) ? 1 : 0;
			}
		}
	}

	/*
	 * �����
	 */
	void zeroMatrix() {
		for (int i = 0; i < nRow * nCol; ++i) {
			matrix[i] = 0;
		}
	}
	
	/*
	 * �����ת��
	 */
	Matrix transpose() {
		Matrix T = new Matrix(nCol, nRow);
		for (int i = 0; i < nRow * nCol; ++i) {
			for (int j = 0; j < nCol; ++j) {
				T.matrix[j * nRow + i] = matrix[i * nCol + j];
			}
		}
		return T;
	}
	
	/*
	 * ��������
	 */
	void resize(int row, int col){
		matrix = null;
		nRow = row;
		nCol = col;
		matrix = new double[nRow * nCol];
		zeroMatrix();
	}
	
	/*
	 * �������Ӿ���
	 */
	
}
