/*  Student information for assignment:
 *
 *  On <OUR> honor, <Melody Rodriguez> and <Carlos Olvera>, this programming assignment is <OUR> own work
 *  and <WE> have not provided this code to any other student.
 *
 *  Number of slip days used: 0
 *
 *  Student 1 (Student whose Canvas account is being used): Melody Rodriguez
 *  UTEID: mar9688
 *  email address: mar9688@utexas.edu
 *  Grader name: Bersam
 *
 *  Student 2: Carlos Olvera
 *  UTEID: cao2546
 *  email address: carlosolvera539@gmail.com
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class SimpleHuffProcessor implements IHuffProcessor {
	private IHuffViewer myViewer;
	private Map<Integer, String> huffMap; // Map for HuffMan Code and ASCII Values
	private int[] frequencies; // Frequencies of the ASCII values
	private int format; // HeaderFormat number
	private TreeNode root; // Main root of Tree
	private int uncompressedBitCount; // Uncompressed Bit Count

	/**
	 * Preprocess data so that compression is possible --- count characters/create
	 * tree/store state so that a subsequent call to compress will work. The
	 * InputStream is <em>not</em> a BitInputStream, so wrap it int one as needed.
	 * 
	 * @param in           is the stream which could be subsequently compressed
	 * @param headerFormat a constant from IHuffProcessor that determines what kind
	 *                     of header to use, standard count format, standard tree
	 *                     format, or possibly some format added in the future.
	 * @return number of bits saved by compression or some other measure Note, to
	 *         determine the number of bits saved, the number of bits written
	 *         includes ALL bits that will be written including the magic number,
	 *         the header format number, the header to reproduce the tree, AND the
	 *         actual data.
	 * @throws IOException if an error occurs while reading from the input file.
	 */
	public int preprocessCompress(InputStream in, int headerFormat) throws IOException {
		int uncompressedBitCount = 0;
		BitInputStream bitIn = new BitInputStream(in);
		PriorityQueue queue = new PriorityQueue();
		root = null;
		frequencies = new int[ALPH_SIZE];
		format = headerFormat;
		boolean keepGoing = true;
		int leafCount = 0;

		while (keepGoing) {
			int value = bitIn.readBits(BITS_PER_WORD);
			uncompressedBitCount += BITS_PER_WORD;
			if (value != -1) {
				frequencies[value]++;
			}
			else {
				keepGoing = false;
			}
		}

		for (int i = 0; i < ALPH_SIZE; i++) {
			//how many times every bit-sequence occurs in the file
			if (frequencies[i] > 0) {
				queue.enqueue(i,frequencies[i]);
				leafCount++;
			}
		}

		queue.enqueue(PSEUDO_EOF, 1);
		root = queue.combine();
	
		// The map has the 8-bit int chunks as keys and the corresponding 
		//Huffman/chunk-coding String as the value associated with the key.
		 HuffMapMaker chunks  = new HuffMapMaker();
		 huffMap = chunks.createHuffMap(root);
		 
		this.uncompressedBitCount = uncompressedBitCount;

		return uncompressedBitCount - getCompressedBitCount(leafCount);
	}

	/**
	 * Calculates the total number of bits that will be written in the compressed file
	 *
	 * @param leafCount the total number of leaves
	 * @return the total number of bits writing int the compressed file
	 */
	public int getCompressedBitCount(int leafCount) {
		boolean keepGoing = true;
		int writtenBitCount = 0;

		// Adds the 32 bits for magic number and header
		writtenBitCount += BITS_PER_INT;
		writtenBitCount += BITS_PER_INT;

		if (format == STORE_COUNTS) {
			// Calculate the amount of the bits written for store counts
			writtenBitCount += (BITS_PER_INT * ALPH_SIZE);
		}
		else if (format == STORE_TREE) {
			// Calculates the amount of bits written for the tree
			writtenBitCount += ((leafCount * BITS_PER_WORD + 1) + treeSize(root));
		}

		// Calculates the bits that will be written in the contents
		for (int i = 0; i < ALPH_SIZE; i++) {
			if (frequencies[i] > 0) {
				int bitCount = (frequencies[i] * huffMap.get(i).length());
				writtenBitCount += bitCount;
			}
		}

		writtenBitCount += huffMap.get(PSEUDO_EOF).length();

		return writtenBitCount;
	}
	
	
	/**
	 * Compresses input to output, where the same InputStream has previously been
	 * pre-processed via <code>preprocessCompress</code> storing state used by this
	 * call. <br>
	 * pre: <code>preprocessCompress</code> must be called before this method
	 * 
	 * @param in    is the stream being compressed 01110 1(NOT a BitInputStream)
	 * @param out   is bound to a file/stream to which bits are written for the
	 *              compressed file (not a BitOutputStream)
	 * @param force if this is true create the output file even if it is larger than
	 *              the input file. If this is false do not create the output file
	 *              if it is larger than the input file.
	 * @return the number of bits written.
	 * @throws IOException if an error occurs while reading from the input file or
	 *                     writing to the output file.
	 */
	public int compress(InputStream in, OutputStream out, boolean force) throws IOException {
		BitInputStream bitInput = new BitInputStream(in);
		BitOutputStream bitOutput= new BitOutputStream(out);
		int bitsWritten = 0;
		boolean keepGoing = true;

		bitOutput.writeBits(BITS_PER_INT, MAGIC_NUMBER);
		bitsWritten += BITS_PER_INT;
		
		bitOutput.writeBits(BITS_PER_INT,format);
		bitsWritten += BITS_PER_INT;
		
		if (format == STORE_COUNTS) {
			for (int i = 0; i < ALPH_SIZE; i++) {
				bitOutput.writeBits(BITS_PER_INT, frequencies[i]);
				bitsWritten += BITS_PER_INT;
			}
		}
		else if (format == STORE_TREE) {
			bitOutput.writeBits(BITS_PER_INT, treeSize(root));
			bitsWritten += BITS_PER_INT;
			bitsWritten += flattenTree(bitOutput,root);
		}
		else {
			throw new IOException("Error: Not proper Format");
		}

		bitsWritten += writeOutputText(bitInput, bitOutput);

		bitOutput.close();

		if (bitsWritten > uncompressedBitCount && !force) {
			myViewer.showError("Compressed file has "  + (bitsWritten - uncompressedBitCount) + " more bits than "
					+ "uncompressed file. Select \"force compression\" option to compress.");
		}

		return bitsWritten;
	}

	/**
	 * Writes the actual text in the huffman bit code developed from the frequency
	 *
	 * @param bitInput, the BitInputStream for reading the file
	 * @param bitOutput, the BItOutputStream for writing the file
	 * @return the total amout of bits written
	 * @throws IOException if an error occurs throw this exception
	 */
	public int writeOutputText(BitInputStream bitInput, BitOutputStream bitOutput) throws IOException {
		boolean keepGoing = true;
		int bitsWritten = 0;
		String eof_huffCode = huffMap.get(PSEUDO_EOF);

		// Writes all the contents of the file
		while (keepGoing) {
			int value = bitInput.readBits(BITS_PER_WORD);
			if (value != -1) {
				String written = huffMap.get(value);

				// Manually get each bit from the string and writes it
				for (int i = 0; i < written.length(); i++) {
					int bit = Integer.parseInt(written.substring(i, i + 1));
					bitOutput.writeBits(1, bit);
				}

				bitsWritten += (written.length());
			}
			// Ran out of bits to read
			else {
				keepGoing = false;
			}
		}

		for (int i = 0; i < eof_huffCode.length(); i++) {
			int bit = Integer.parseInt(eof_huffCode.substring(i, i + 1));
			bitOutput.writeBits(1, bit);
		}

		bitsWritten += eof_huffCode.length();

		return bitsWritten;
	}

	/**
	 * Recursively flattens the tree and writes it to the file
	 * @param output BitOutputStream for wrtiting the file
	 * @param currentNode the CurrentNode that the recursive method is on
	 * @return the total amout of bits written
	 */
	public int flattenTree(BitOutputStream output, TreeNode currentNode) {
		// Base Case
		if (currentNode.isLeaf()) {
			output.writeBits(1,1);
			output.writeBits(BITS_PER_WORD + 1, currentNode.getValue());
			return BITS_PER_WORD + 2;
		}
		// Recursive Case
		else {
			if (currentNode.getLeft() != null && currentNode.getRight() == null) {
				output.writeBits(1,0);
				return 1 + flattenTree(output, currentNode.getLeft());
			}
			else if (currentNode.getLeft() == null && currentNode.getRight() != null) {
				output.writeBits(1,0);
				return 1 + flattenTree(output, currentNode.getRight());
			}
			else {
				output.writeBits(1,0);
				return 1 + flattenTree(output, currentNode.getLeft()) + flattenTree(output, currentNode.getRight());
			}
		}
	}

	/**
	 * Recursively finds the total size of the tree
	 *
	 * @param currentNode the currentNode the recursive method is on
	 * @return the total size of the tree
	 */
	public int treeSize(TreeNode currentNode) {
		// Base Case
		if (currentNode.isLeaf()) {
			return 1 + (1 + BITS_PER_WORD);
		}
		// Recursive Case
		else {
			if (currentNode.getLeft() != null && currentNode.getRight() == null) {
				return 1 + treeSize(currentNode.getLeft());
			}
			else if (currentNode.getLeft() == null && currentNode.getRight() != null) {
				return 1 + treeSize(currentNode.getRight());
			}
			else {
				return treeSize(currentNode.getRight()) + 1 + treeSize(currentNode.getLeft());
			}
		}
		
	}
	

	/**
	 * Uncompress a previously compressed stream in, writing the uncompressed
	 * bits/data to out.
	 * 
	 * @param in  is the previously compressed data (not a BitInputStream)
	 * @param out is the uncompressed file/stream
	 * @return the number of bits written to the uncompressed file/stream
	 * @throws IOException if an error occurs while reading from the input file or
	 *                     writing to the output file.
	 */
	public int uncompress(InputStream in, OutputStream out) throws IOException {
		BitInputStream bitInputStream = new BitInputStream(in);
		BitOutputStream bitOutputStream = new BitOutputStream(out);
		TreeNode root = null;

		int magic = bitInputStream.readBits(BITS_PER_INT);
		if (magic != MAGIC_NUMBER) {
			throw new IOException("Magic Number not implemented properly");
		}

		int format = bitInputStream.readBits(BITS_PER_INT);

		if (format == STORE_COUNTS) {
			PriorityQueue queue = new PriorityQueue();

			for (int i = 0; i < ALPH_SIZE; i++) {
				int frequencyInOriginalFile = bitInputStream.readBits((BITS_PER_INT));
				// Only care about values contained in file
				if (frequencyInOriginalFile > 0) {
					queue.enqueue(i, frequencyInOriginalFile);
				}
			}

			queue.enqueue(PSEUDO_EOF, 1);
			root = queue.combine();
		}
		else if (format == STORE_TREE) {
			int treeSize = bitInputStream.readBits(BITS_PER_INT);
			root = buildTree(bitInputStream);
		}

		return unhuff(bitInputStream, bitOutputStream, root);
	}

	/**
	 * Takes the map and writes in the file based on mapKeys which converts the
	 * HuffMan values into ASCII values
	 * 
	 * @param in   the content of the previously compressed file
	 * @param out  is the uncompressed file/stream
	 * @param root the starting treeNode
	 * @return the total amount of bits written
	 * @throws IOException
	 */
	public int unhuff (BitInputStream in, BitOutputStream out, TreeNode root) throws IOException {
		int bitsWrittenCount = 0;
		TreeNode tempNode = root;
		boolean keepGoing = true;

		while (keepGoing) {
			int bit = in.readBits(1);
			// There isn't any bits left to read
			if (bit == -1) {
				throw new IOException ("Error reading compressed file. \n"
						+ "unexpected end of input." + " No PSEUDO_EOF value.");
			}
			else {
				if (bit == 0) {
					tempNode = tempNode.getLeft();
				}
				else if (bit == 1) {
					tempNode = tempNode.getRight();
				}
				if (tempNode.isLeaf()) {
					// Reached end of file
					if (tempNode.getValue() == PSEUDO_EOF) {
						keepGoing = false;
					}
					else {
						out.writeBits(BITS_PER_WORD, tempNode.getValue());
						tempNode = root;
						bitsWrittenCount += BITS_PER_WORD;
					}
				}
			}
		}

		return bitsWrittenCount;
	}

	/**
	 * Builds the tree from a compressed tree
	 *
	 * @param input BitInputStream for reading the file
	 * @return node for the tree
	 * @throws IOException if we ran out of bits while building a tree
	 */
	public TreeNode buildTree(BitInputStream input) throws IOException {
		final int EMPTY = -1;
		int bit = input.readBits(1);
		if (bit == 0) {
			// Internal Node
			TreeNode node = new TreeNode(EMPTY, 0);
			node.setLeft(buildTree(input));
			node.setRight(buildTree(input));
			return node;
		}
		else if (bit == 1) {
			// This is a leaf
			int value = input.readBits(BITS_PER_WORD + 1);
			return new TreeNode(value, 0);
		}
		else {
			throw new IOException("Ran out of Bits, Error while creating tree.");
		}
	}

	public void setViewer(IHuffViewer viewer) {
		myViewer = viewer;
	}

	private void showString(String s) {
		if (myViewer != null) {
			myViewer.update(s);
		}
	}
}