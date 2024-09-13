import unittest
import main

def test_sum_bits():
    assert main.sum_bits(1, 1) == 1
    assert main.sum_bits(2, 1) == 0
    assert main.sum_bits(0, 1) == 0
    assert main.sum_bits(3, 1) == 1
    assert main.sum_bits(4,1) == 0
    assert main.sum_bits(5,1) == 1
    assert main.sum_bits(7,1) == 1
    assert main.sum_bits(1,2) == 0
    assert main.sum_bits(2,2) == 1
    assert main.sum_bits(4,2) == 0
    assert main.sum_bits(3,3) == 0
    assert main.sum_bits(5,3) == 1
    
    
if __name__ == '__main__':
    test_sum_bits()