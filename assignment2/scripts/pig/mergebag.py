from pig_util import outputSchema

@outputSchema("to_filenames:bag")
def merge_bag(filename, filename_bags):
    merged_bag = set()
    
    from_filename = set([(filename,)])
    to_filenames = []
    for filename_bag in filename_bags:
        to_filenames.extend(filename_bag[0])

    merged_bag = set(to_filenames)
    merged_bag.difference_update(from_filename)

    return list(merged_bag)
