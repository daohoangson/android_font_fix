import glob
import hashlib
import os

def clean_up():
  os.system('mkdir res/raw')
  os.system('rm res/raw/*.ttf')

def copy():
  ttf_paths = glob.glob('font_roboto/*.ttf')
  for ttf_path in ttf_paths:
    ttf_name = os.path.basename(ttf_path)
    res_name = ttf_name.lower().replace('-', '_')
    res_path = 'res/raw/%s' % (res_name)
    os.system('cp %s %s' % (ttf_path, res_path))

def _calc_hash(path):
  f = open(path, 'rb')
  hasher = hashlib.md5()
  buf_size = 65536
  
  buf = f.read(buf_size)
  while len(buf) > 0:
    hasher.update(buf)
    buf = f.read(buf_size)

  return hasher.hexdigest()

def generate_hashes():
  res_paths = glob.glob('res/raw/*.ttf')
  for res_path in res_paths:
    res_id = os.path.basename(res_path)[:-4]
    h = _calc_hash(res_path)
    print 'hashes.put(R.raw.%s, "%s");' % (res_id, h)

if __name__ == '__main__':
  clean_up()
  copy()
  
  print '\nHashes (Java):'
  generate_hashes()  