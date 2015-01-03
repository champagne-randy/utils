# interface to be implemented for each type of command file desired
class Formatter
    attr_reader :commands
    
    def initialize(operation)
      @commands = Array.new
      #raise 'not yet implemented'
    end
    
    #build table/command file lines
    def build_commands(recordings)
      recordings.each { |recording| 
        @commands.push ",#{recording.su_id}"
      }
      #raise 'not yet implemented'
    end
    
    #write to output file  
    def write_to(output_file)
      file = File.open(output_file, "w") 
      @commands.each { |command| file.write(command.to_s + "\n") }
      file.close 
      #raise 'not yet implemented'
    end
end