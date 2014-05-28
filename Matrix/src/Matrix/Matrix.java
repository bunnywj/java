package Matrix;

public class Matrix {

	private int nRow;
	private int nCol;
	private double[] matrix;

	/*
	 * 构造函数
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
	 * 矩阵释放空间
	 */
	void release() {
		matrix = null;
	}

	/* 
	 * 取第（i,j）个元素
	 */
	double get(int i, int j) {

		return matrix[i * nCol + j];
	}

	/* 
	 * 计算矩阵长度
	 */
	int length() {
		return nRow * nCol;
	}

	/* 
	 * 存入第（i，j）个元素
	 */
	void set(int idx, double val) {
		matrix[idx] = val;
	}
	
	/*
	 * 显示矩阵
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
	 * 矩阵相加
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
	 * 矩阵相减
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
	 *  矩阵相乘
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
	 * 矩阵与实数相加
	 */
	Matrix addReal(double val) {
		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] + val;
		}
		return result;
	}

	/*
	 * 矩阵与实数相减
	 */
	Matrix subReal(double val) {
		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] - val;
		}
		return result;
	}

	/*
	 * 矩阵与实数相乘
	 */
	Matrix mulReal(double val) {
		Matrix result = new Matrix(nRow, nCol);
		for (int i = 0; i < nRow * nCol; ++i) {
			result.matrix[i] = matrix[i] * val;
		}
		return result;
	}

	/*
	 *  矩阵与实数相除
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
	 * 单位矩阵
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
	 * 零矩阵
	 */
	void zeroMatrix() {
		for (int i = 0; i < nRow * nCol; ++i) {
			matrix[i] = 0;
		}
	}
	
	/*
	 * 矩阵的转置
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
	 * 矩阵重置
	 */
	void resize(int row, int col){
		matrix = null;
		nRow = row;
		nCol = col;
		matrix = new double[nRow * nCol];
		zeroMatrix();
	}
	
	/*
	 * 矩阵求子矩阵
	 */
	
}
