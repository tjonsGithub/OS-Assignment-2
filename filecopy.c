#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>

/* this function reads data from the input stream
 * and save the data to the output stream. */
void forward(int in, int out);

int main(int argc, char *argv[]) {
  int p[2];
  int child;
  int fileno;

  if (argc != 3) {
    printf("usage: %s arg1 arg2\n", argv[0]);
    exit(-1);
  }

  /* create the pipe to send data. */
  if (pipe(p) == -1) {
    perror("could not create the pipe.");
    exit(-1);
  }

  /* create the child process. */
  child = fork();
  if (child < 0) {
    perror("could not create child process.");
    exit(-1);
  }
  if (child > 0) {
    /* parent process: open and read the original file,
     * forward it to the write side of the pipe */
    close(p[0]);
    fileno = open(argv[1], O_RDONLY);
    if (fileno < 0) {
      fprintf(stdout, "could not read file %s.\n", argv[1]);
    } else {
      forward(fileno, p[1]);
      close(fileno);
    }
    close(p[1]);
    /* terminate the program when the child has saved all the data */
    waitpid(child, NULL, 0);

  } else {
    /* child process: read from the read side of the pipe,
     * open and save the data into the copy file (the file allows read/write) */
    close(p[1]);
    fileno = open(argv[2], O_WRONLY | O_CREAT | O_TRUNC, 0666);
    if (fileno < 0) {
      fprintf(stdout, "could not write file %s.\n", argv[2]);
    } else {
      forward(p[0], fileno);
      close(fileno);
    }
    close(p[0]);
  }

  return 0;
}

void forward(int in, int out) {
  char data[1024];
  ssize_t size;

  /* read from the input stream until the end of file */
  while ((size = read(in, data, sizeof(data))) > 0) {
    /* save into the output stream */
    ssize_t bytes = 0;
    while (bytes < size) {
      ssize_t written = write(out, data, size);
      if (written < 0) {
        perror("could not forward data.");
        break;
      }
      bytes += written;
    }
  }
}
